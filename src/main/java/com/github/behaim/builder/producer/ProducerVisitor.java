/*
 * Copyright (C) 2010-2016 Christian Gleissner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.behaim.builder.producer;

import com.github.behaim.builder.adapter.SeedAdapter;
import com.github.behaim.builder.seeder.Seeder;
import com.github.behaim.explorer.FieldUtil;
import com.github.behaim.explorer.VisitationResult;
import com.github.behaim.explorer.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerVisitor implements Visitor {

    private final static class CreationResult {
        private final Object createdObject;
        private final Class elementClass;

        public CreationResult(Object createdObject, Class elementClass) {
            this.createdObject = createdObject;
            this.elementClass = elementClass;
        }
    }

    private final static Map<Class<?>, Class<?>> CONCRETE_COLLECTION_CLASSES = new HashMap<Class<?>, Class<?>>();
    private final static Logger logger = LoggerFactory.getLogger(ProducerVisitor.class);

    static {
        CONCRETE_COLLECTION_CLASSES.put(Collection.class, ArrayList.class);
        CONCRETE_COLLECTION_CLASSES.put(List.class, ArrayList.class);
        CONCRETE_COLLECTION_CLASSES.put(Set.class, HashSet.class);
        CONCRETE_COLLECTION_CLASSES.put(Queue.class, ArrayBlockingQueue.class);
    }

    private final ProducerVisitorContext context;

    public ProducerVisitor(ProducerVisitorContext producerVisitorContext) {
        context = producerVisitorContext;
    }

    private CreationResult createObjectFor(Field field) {
        Object fieldValue = null;
        Class elementClass = null;
        // TODO gleissc Obtain from configuration
        int collectionSize = 3;
        try {
            if (isCollection(field)) {
                Class collectionSubclass = getConcreteCollectionClassFor(field.getType());
                if (collectionSubclass != null) {
                    Collection collection = (Collection) collectionSubclass.newInstance();
                    elementClass = getCollectionParameter(field);
                    for (int i = 0; i < collectionSize; i++) {
                        collection.add(elementClass.newInstance());
                    }
                    fieldValue = collection;
                }
            } else if (isArray(field)) {
                Object[] array = new Object[collectionSize];
                Class elementType = field.getType().getComponentType();
                for (int i = 0; i < collectionSize; i++) {
                    array[i] = elementType.newInstance();
                }
                fieldValue = array;
            } else if (!field.getType().isEnum()) {
                fieldValue = field.getType().newInstance();
            }
        } catch (Exception e) {
            logger.warn("Could not instantiate field", FieldUtil.getNameAndTypeFor(field), e);
        }
        return new CreationResult(fieldValue, elementClass);
    }

    private Class getCollectionParameter(Field field) {
        Class fieldParameterClass = null;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length >= 1) {
                fieldParameterClass = (Class) actualTypeArguments[0];
            }
        }
        return fieldParameterClass;
    }

    private Class getConcreteCollectionClassFor(Class<?> clazz) {
        Class subclass = CONCRETE_COLLECTION_CLASSES.get(clazz);
        if (subclass == null) {
            if (!Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers())) {
                subclass = clazz;
            }
        }
        return subclass;
    }

    private boolean isArray(Field field) {
        return field.getType().isArray();
    }

    private boolean isClassExcluded(Class clazz) {
        boolean excluded = false;
        String className = clazz.getName();
        for (String excludedClassName : context.getConfig().getExcludedClassNames()) {
            if (className.startsWith(excludedClassName)) {
                excluded = true;
                break;
            }
        }
        return excluded;
    }

    private boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    @Override
    public VisitationResult visit(Object object, Field field) {
        Object fieldValue = null;
        Class fieldClass = field.getType();
        ProducerFieldContext fieldContext = context.getFieldContextFor(field);
        Seeder seeder = fieldContext.getSeeder();
        SeedAdapter<?> seedAdapter = context.getSeedAdapterFor(field);
        boolean visitOfValueRequired = true;
        if (seedAdapter != null) {
            fieldValue = seedAdapter.convert(seeder);
            visitOfValueRequired = !isClassExcluded(fieldClass);
        }
        if (fieldValue == null) {
            CreationResult creationResult = createObjectFor(field);
            fieldValue = creationResult.createdObject;
        }
        try {
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (Exception e) {
            logger.warn("Could not set " + FieldUtil.getNameFor(field) + "=" + fieldValue + " provided by "
                    + seedAdapter + " on " + object, e);
        }
        return new VisitationResult(fieldContext, fieldValue, visitOfValueRequired);
    }
}
