package com.googlecode.behaim.builder.seeder;

import com.googlecode.behaim.builder.config.FieldConfig;

public class SeederFactory {

	public Seeder createSeeder(FieldConfig fieldConfig) {
		Seeder seeder = null;
		if (fieldConfig.isRandom()) {
			seeder = new RandomSeeder(fieldConfig);
		} else {
			seeder = new SequentialSeeder(fieldConfig);
		}
		return seeder;
	}
}
