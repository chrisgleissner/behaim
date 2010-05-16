package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public class FloatAdapter extends AbstractSeedAdapter<Float> {

	@Override
	public Float convert(Seeder seeder) {
		return new Float(seeder.createSeed());
	}

	@Override
	public Class<Float> getValueClass() {
		return Float.class;
	}
}
