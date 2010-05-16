package com.googlecode.behaim.builder.adapter;

import com.googlecode.behaim.builder.seeder.Seeder;

public interface SeedAdapter<T> {

	T convert(Seeder seeder);

	Class<T> getValueClass();
}
