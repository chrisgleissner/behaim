package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public class IntegerAdapter extends AbstractSeedAdapter<Integer> {

	@Override
	public Integer convert(Seeder seeder) {
		return new Integer((int) seeder.createSeed());
	}

	@Override
	public Class<Integer> getValueClass() {
		return Integer.class;
	}
}
