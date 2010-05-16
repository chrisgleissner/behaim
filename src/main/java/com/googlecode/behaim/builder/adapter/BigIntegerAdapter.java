package com.googlecode.behaim.builder.adapter;

import java.math.BigInteger;

import com.googlecode.behaim.builder.seeder.Seeder;

public class BigIntegerAdapter extends AbstractSeedAdapter<BigInteger> {

	@Override
	public BigInteger convert(Seeder seeder) {
		return new BigInteger("" + (long) seeder.createSeed());
	}

	@Override
	public Class<BigInteger> getValueClass() {
		return BigInteger.class;
	}
}
