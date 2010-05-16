package com.googlecode.behaim.builder.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.googlecode.behaim.explorer.Visitor;
import com.googlecode.behaim.route.Route;

public final class Producer<T> implements Callable<List<T>> {

	private final int numberOfInstances;
	private final Route route;
	private final Class<T> type;
	private final Visitor visitor;

	public Producer(Route route, Visitor visitor, Class<T> type, int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
		this.type = type;
		this.route = route;
		this.visitor = visitor;
	}

	@Override
	public List<T> call() throws Exception {
		List<T> instances = new ArrayList<T>(numberOfInstances);
		for (int i = 0; i < numberOfInstances; i++) {
			T t = type.newInstance();
			route.prepareTrip(t, visitor).perform();
			instances.add(t);
		}
		return instances;
	}

}
