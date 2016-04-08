package com.github.behaim.utils;

import com.github.behaim.domain.Person;

import java.lang.reflect.Field;

/**
 * @author Fabien DUMINY
 */
public enum PersonFields {
    NAME("name"),
    TEAM("team"),
    PREFERRED_COLOR("preferredColor"),
    EMPTY_ENUM("alwaysNullValue"),
    SINGLE_VALUE_ENUM("singleValue");

    private final Field field;

    private PersonFields(String field) {
        try {
            this.field = Person.class.getDeclaredField(field);
            this.field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Field getField() {
        return field;
    }
}
