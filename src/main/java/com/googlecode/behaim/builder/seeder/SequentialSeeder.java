package com.googlecode.behaim.builder.seeder;

import com.googlecode.behaim.builder.config.FieldConfig;

public class SequentialSeeder extends AbstractSeeder {

	private int nextSeed;

	public SequentialSeeder(FieldConfig fieldConfig) {
		super(fieldConfig);
		nextSeed = fieldConfig.getMinValue();
	}

	@Override
	public double createSeed() {
		FieldConfig fieldConfig = getConfig();
		double seed = nextSeed++;
		if (nextSeed > fieldConfig.getMaxValue()) {
			nextSeed = fieldConfig.getMinValue();
		}
		return seed;
	}
}
