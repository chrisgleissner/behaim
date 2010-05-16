package com.googlecode.behaim.explorer;

import java.lang.reflect.Field;

public class FieldUtil {
	public static String getNameAndTypeFor(Field field) {
		return getNameFor(field) + ":" + getTypeNameFor(field);
	}

	public static String getNameFor(Field field) {
		return field.getDeclaringClass().getSimpleName() + "." + field.getName();
	}

	public static String getTypeNameFor(Field field) {
		return field.getType().getName();
	}
}
