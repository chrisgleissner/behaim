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
package com.github.chrisgleissner.behaim.builder.producer;

import com.github.chrisgleissner.behaim.builder.adapter.AdapterRegistry;
import com.github.chrisgleissner.behaim.builder.adapter.EnumAdapter;
import com.github.chrisgleissner.behaim.builder.adapter.SeedAdapter;
import com.github.chrisgleissner.behaim.builder.config.Config;
import com.github.chrisgleissner.behaim.builder.config.FieldConfig;
import com.github.chrisgleissner.behaim.builder.seeder.Seeder;
import com.github.chrisgleissner.behaim.builder.seeder.SeederFactory;
import com.github.chrisgleissner.behaim.explorer.FieldUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProducerVisitorContext {

    private final Config config;
    private final ConcurrentMap<Field, ProducerFieldContext> producerFieldContexts = new ConcurrentHashMap<Field, ProducerFieldContext>();
    @SuppressWarnings("unchecked")
    private final Map<Class, SeedAdapter> seedAdapters = new HashMap<Class, SeedAdapter>();
    private final SeederFactory seederFactory = new SeederFactory();

    public ProducerVisitorContext() {
        this(Config.DEFAULT_CONFIG);
    }

    public ProducerVisitorContext(Config config) {
        this.config = config;
        AdapterRegistry valueFactoryRegistry = new AdapterRegistry();
        for (SeedAdapter<?> seedAdapter : valueFactoryRegistry.getSeedAdapters()) {
            seedAdapters.put(seedAdapter.getValueClass(), seedAdapter);
        }
    }

    public Config getConfig() {
        return config;
    }

    public ProducerFieldContext getFieldContextFor(Field field) {
        ProducerFieldContext producerFieldContext = producerFieldContexts.get(field);
        if (producerFieldContext == null) {
            FieldConfig fieldConfig = config.getFieldConfigFor(FieldUtil.getNameFor(field));
            if (field.getType().isEnum()) {
                int maxValue = field.getType().getEnumConstants().length;
                fieldConfig = new FieldConfig(fieldConfig.isRandom(), 0, maxValue, 0, 0);
            }
            Seeder seeder = seederFactory.createSeeder(fieldConfig);
            producerFieldContext = new ProducerFieldContext(field, seeder);
            ProducerFieldContext existingFieldContext = producerFieldContexts.putIfAbsent(field, producerFieldContext);
            if (existingFieldContext != null) {
                producerFieldContext = existingFieldContext;
            }
        }
        return producerFieldContext;
    }

    public SeedAdapter<?> getSeedAdapterFor(Field field) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            type = TypeUtil.wrap(type);
        }
        if (field.getType().isEnum()) {
            return EnumAdapter.forClass(field.getType().asSubclass(Enum.class));
        }
        return seedAdapters.get(type);
    }
}
