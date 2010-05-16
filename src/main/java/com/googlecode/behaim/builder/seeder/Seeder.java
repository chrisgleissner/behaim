package com.googlecode.behaim.builder.seeder;

import com.googlecode.behaim.builder.config.FieldConfig;

public interface Seeder {

	double createSeed();

	FieldConfig getConfig();
}
