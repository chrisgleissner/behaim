package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public class BooleanAdapter extends AbstractSeedAdapter<Boolean> {

	@Override
	public Boolean convert(Seeder seeder) {
		return new Boolean((long) seeder.createSeed() % 2 == 1);
	}

	@Override
	public Class<Boolean> getValueClass() {
		return Boolean.class;
	}
}
