package com.github.chrisgleissner.behaim.utils;

import java.lang.reflect.Field;

/**
 * @author Fabien DUMINY
 */
public class Objects {
    public static boolean equals(Object object1, Object object2) {
        return object1 == object2 || object1.equals(object2);
    }

    public static int hash(Object object, Field field) {
        if (field == null) {
            return 0;
        }
        if (object == null) {
            return field.hashCode();
        }

        Object fieldValue = null;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            return 0;
        }
        return (fieldValue == null) ? 0 : fieldValue.hashCode();
    }
}
