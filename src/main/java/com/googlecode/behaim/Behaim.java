package com.googlecode.behaim;

import java.util.Collection;

import com.googlecode.behaim.builder.Builder;
import com.googlecode.behaim.builder.config.Config;

public class Behaim<T> {

	public final static <T> T build(Class<T> type) {
		return new Builder<T>(type).build();
	}

	public final static <T> Collection<T> build(Class<T> type, int numberOfInstances) {
		return new Builder<T>(type).build(numberOfInstances);
	}

	public final static <T> Collection<T> build(Class<T> type, int numberOfInstances, Config config) {
		return new Builder<T>(type, config).build(numberOfInstances);
	}

}
