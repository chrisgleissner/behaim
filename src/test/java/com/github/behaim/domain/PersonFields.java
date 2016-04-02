package com.github.behaim.domain;

import java.lang.reflect.Field;

/**
 * @author Fabien DUMINY
 */
public class PersonFields {
    public static final Field NAME = field("name");
    public static final Field TEAM = field("team");
    public static final Field PREFERRED_COLOR = field("preferredColor");
    public static final Field EMPTY_ENUM = field("alwaysNullValue");
    public static final Field SINGLE_VALUE_ENUM = field("singleValue");

    private PersonFields() {
    }

    private static Field field(String fieldName) {
        try {
            final Field declaredField = Person.class.getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
