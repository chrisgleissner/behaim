package com.googlecode.behaim.builder.seeder;

import java.util.Random;

import com.googlecode.behaim.builder.config.FieldConfig;

public class RandomSeeder extends AbstractSeeder {

	private final Random random = new Random();

	public RandomSeeder(FieldConfig fieldConfig) {
		super(fieldConfig);
	}

	@Override
	public double createSeed() {
		FieldConfig fieldConfig = getConfig();
		return random.nextDouble() * (fieldConfig.getMaxValue() - fieldConfig.getMinValue()) + fieldConfig.getMinValue();
	}
}
