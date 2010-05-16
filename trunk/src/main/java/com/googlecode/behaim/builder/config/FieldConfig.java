package com.googlecode.behaim.builder.config;

public class FieldConfig {

	private int maxLength = 10;
	private int maxValue = Integer.MAX_VALUE;
	private int minLength = 3;
	private int minValue = 0;
	private boolean random = true;
	// TODO gleissc Add support for unique random values, then expose this field
	private final boolean unique = false;

	public FieldConfig() {
		super();
	}

	public FieldConfig(boolean random, int minValue, int maxValue, int minLength, int maxLength) {
		super();
		this.random = random;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public int getMinLength() {
		return minLength;
	}

	public int getMinValue() {
		return minValue;
	}

	public boolean isRandom() {
		return random;
	}

	public boolean isUnique() {
		return unique;
	}
}
