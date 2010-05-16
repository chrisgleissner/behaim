package com.googlecode.behaim.builder.adapter;

import java.math.BigDecimal;

import com.googlecode.behaim.builder.seeder.Seeder;

public class BigDecimalAdapter extends AbstractSeedAdapter<BigDecimal> {

	@Override
	public BigDecimal convert(Seeder seeder) {
		return new BigDecimal("" + seeder.createSeed());
	}

	@Override
	public Class<BigDecimal> getValueClass() {
		return BigDecimal.class;
	}
}
