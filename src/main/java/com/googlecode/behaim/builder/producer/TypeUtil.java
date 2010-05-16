package com.googlecode.behaim.builder.producer;

import java.util.HashMap;
import java.util.Map;

public class TypeUtil {

	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_MAP = new HashMap<Class<?>, Class<?>>();

	static {
		PRIMITIVE_TO_WRAPPER_MAP.put(boolean.class, Boolean.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(char.class, Character.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(byte.class, Byte.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(short.class, Short.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(int.class, Integer.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(long.class, Long.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(float.class, Float.class);
		PRIMITIVE_TO_WRAPPER_MAP.put(double.class, Double.class);

	}

	public final static Class<?> wrap(Class<?> primitiveType) {
		return PRIMITIVE_TO_WRAPPER_MAP.get(primitiveType);
	}
}
