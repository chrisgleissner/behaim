package com.googlecode.behaim.builder.seeder;

import com.googlecode.behaim.builder.config.FieldConfig;

public abstract class AbstractSeeder implements Seeder {

	private final FieldConfig fieldConfig;

	public AbstractSeeder(FieldConfig fieldConfig) {
		this.fieldConfig = fieldConfig;
	}

	public FieldConfig getConfig() {
		return fieldConfig;
	}
}
