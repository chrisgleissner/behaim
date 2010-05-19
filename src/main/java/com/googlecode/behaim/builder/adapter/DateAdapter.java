package com.googlecode.behaim.builder.adapter;

import java.util.Date;

import com.googlecode.behaim.builder.seeder.Seeder;

public class DateAdapter extends AbstractSeedAdapter<Date> {

	@Override
	public Date convert(Seeder seeder) {
		return new Date((long) seeder.createSeed() * 1000);
	}

	@Override
	public Class<Date> getValueClass() {
		return Date.class;
	}
}
