package com.github.chrisgleissner.behaim.stats;

import com.github.chrisgleissner.behaim.explorer.FieldContext;
import com.github.chrisgleissner.behaim.explorer.VisitationResult;
import com.github.chrisgleissner.behaim.explorer.Visitor;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Fabien DUMINY
 */
public class StatisticsVisitor implements Visitor {
    private final HashMap<String, Integer> counters = new HashMap<String, Integer>();

    private final Set<Value> visited = new HashSet<Value>();

    @Override
    public VisitationResult visit(Object object, final Field field) {
        Value valueObj = new Value(object, null);
        if (!visited.contains(valueObj)) {
            visited.add(valueObj);
            incrementCounter(object);
        }

        Value value = new Value(object, field);
        boolean alreadySeen = visited.contains(value);
        boolean visitable = false;
        Object fieldValue = null;
        final Class<?> type = field.getType();
        if (!alreadySeen) {
            visited.add(value);

            fieldValue = getFieldValue(object, field);
            visitable = isVisitable(type);

            incrementCounter(fieldValue);
        }

        boolean visitOfValueRequired = !alreadySeen && visitable;

        return new VisitationResult(new FieldContext() {
            @Override
            public Field getField() {
                return field;
            }
        }, fieldValue, visitOfValueRequired);
    }

    public SortedSet<String> getClasses() {
        return new TreeSet<String>(counters.keySet());
    }

    public int getCount(Class<?> clazz) {
        Integer counter = counters.get(getKey(clazz));
        return (counter == null) ? 0 : counter;
    }

    private boolean isVisitable(Class<?> type) {
        boolean visitable;
        if (type.isPrimitive() || (type.isEnum() && (type.getEnumConstants().length == 0))) {
            visitable = false;
        } else {
            String packageName = type.getPackage().getName();
            visitable = !packageName.startsWith("java.") && !packageName.startsWith("javax.");
        }
        return visitable | Collection.class.isAssignableFrom(type);
    }

    private void incrementCounter(Object object) {
        if (object != null) {
            String key = getKey(object.getClass());
            Integer count = counters.get(key);
            counters.put(key, (count == null) ? 1 : (count + 1));
        }
    }

    private String getKey(Class clazz) {
        return clazz.getName();
    }

    private Object getFieldValue(Object object, final Field field) {
        Object fieldValue = null;
        if (object != null) {
            final boolean wasAccessible = field.isAccessible();
            try {
                if (!wasAccessible) {
                    field.setAccessible(true);
                }
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (!wasAccessible) {
                    field.setAccessible(false);
                }
            }
        }
        return fieldValue;
    }

    private static class Value {
        private final Object object;
        private final Field field;

        private Value(Object object, Field field) {
            this.object = object;
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            return Objects.equals(object, value.object) &&
                    Objects.equals(field, value.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object, field);
        }
    }
}
