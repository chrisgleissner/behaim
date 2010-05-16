package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public class DoubleAdapter extends AbstractSeedAdapter<Double> {

	@Override
	public Double convert(Seeder seeder) {
		return new Double(seeder.createSeed());
	}

	@Override
	public Class<Double> getValueClass() {
		return Double.class;
	}
}
