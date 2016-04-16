package com.github.behaim.utils;

import com.github.behaim.domain.ColorEnum;
import com.github.behaim.domain.Person;
import com.github.behaim.domain.SingleValueEnum;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import static java.util.Collections.emptyList;

/**
 * @author Fabien DUMINY
 */
public enum PersonFields {
    ANNUALSALARY("annualSalary") {
        @Override
        BigDecimal convert(int value) {
            return BigDecimal.valueOf(value);
        }
    },
    BIRTHDAY("birthday") {
        @Override
        Date convert(int value) {
            return new Date(value);
        }
    },
    MANAGER("manager") {
        @Override
        Person convert(int value) {
            Person manager = new Person();
            NAME.setValue(manager, value);
            return manager;
        }
    },
    NAME("name") {
        @Override
        Object convert(int value) {
            return getField().getName() + value;
        }
    },
    TEAM("team") {
        @Override
        Object convert(int value) {
            return emptyList();
        }
    },
    PREFERRED_COLOR("preferredColor") {
        @Override
        Object convert(int value) {
            return ColorEnum.values()[value];
        }
    },
    EMPTY_ENUM("alwaysNullValue") {
        @Override
        Object convert(int value) {
            return null;
        }
    },
    SINGLE_VALUE_ENUM("singleValue") {
        @Override
        Object convert(int value) {
            return SingleValueEnum.SINGLE_VALUE;
        }
    };

    private final Field field;

    PersonFields(String field) {
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

    public final void setValue(Person person, Integer value) {
        try {
            field.set(person, (value == null) ? null : convert(value));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    abstract Object convert(int value);

    public Object getValue(Person person) {
        try {
            return (person == null) ? null : field.get(person);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isVisitable(boolean nullValue) {
        return !nullValue && (this != EMPTY_ENUM);
    }
}
