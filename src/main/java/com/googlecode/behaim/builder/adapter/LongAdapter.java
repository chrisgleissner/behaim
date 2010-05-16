package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public class LongAdapter extends AbstractSeedAdapter<Long> {

	@Override
	public Long convert(Seeder seeder) {
		return new Long((long) seeder.createSeed());
	}

	@Override
	public Class<Long> getValueClass() {
		return Long.class;
	}
}
