package com.googlecode.behaim.builder.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.behaim.BehaimException;

public class AdapterRegistry {
	@SuppressWarnings("unchecked")
	private final static Class[] SEED_ADAPTER_CLASSES = new Class[] { BigDecimalAdapter.class, BigIntegerAdapter.class,
	        BooleanAdapter.class, DateAdapter.class, DoubleAdapter.class, FloatAdapter.class, IntegerAdapter.class,
	        LongAdapter.class, StringAdapter.class };

	@SuppressWarnings("unchecked")
	public Collection<SeedAdapter> getSeedAdapters() {
		List<SeedAdapter> seedAdapters = new ArrayList<SeedAdapter>();
		for (Class seedAdapterClass : SEED_ADAPTER_CLASSES) {
			try {
				seedAdapters.add((SeedAdapter) seedAdapterClass.newInstance());
			} catch (Exception e) {
				throw new BehaimException("Could not instantiate seed adapter " + seedAdapterClass, e);
			}
		}
		return seedAdapters;
	}
}
