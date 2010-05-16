package com.googlecode.behaim.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.Behaim;
import com.googlecode.behaim.BehaimException;
import com.googlecode.behaim.builder.config.Config;
import com.googlecode.behaim.builder.producer.Producer;
import com.googlecode.behaim.builder.producer.ProducerFactory;
import com.googlecode.behaim.builder.producer.ProducerVisitor;
import com.googlecode.behaim.builder.producer.ProducerVisitorContext;
import com.googlecode.behaim.explorer.Explorer;
import com.googlecode.behaim.route.Route;

public class Builder<T> {
	private final static ExecutorService executorService = Executors.newCachedThreadPool();

	private final static Logger logger = LoggerFactory.getLogger(Behaim.class);

	private Config config;
	private final ProducerFactory<T> producerFactory = new ProducerFactory<T>();
	private Route route;
	private Class<T> type;

	public Builder(Class<T> type) {
		this(type, Config.DEFAULT_CONFIG);
	}

	public Builder(Class<T> type, Config config) {
		this.type = type;
		this.config = config;
	}

	public T build() {
		return build(1).iterator().next();
	}

	public Collection<T> build(int numberOfInstances) {
		List<T> instances = new ArrayList<T>(numberOfInstances);
		instances.add(explore());
		if (numberOfInstances > 1) {
			instances.addAll(revisit(numberOfInstances - 1));
		}
		return instances;
	}

	private T explore() {
		ProducerVisitorContext producerVisitorContext = new ProducerVisitorContext(config);
		ProducerVisitor visitor = new ProducerVisitor(producerVisitorContext);
		Explorer explorer = new Explorer(visitor, config.getRecursionDepth());
		T t;
		try {
			t = type.newInstance();
		} catch (Exception e) {
			throw new BehaimException("Could not instantiate " + type, e);
		}
		route = explorer.explore(t);
		logger.trace("Exploration discovered route:\n{}", route);
		return t;

	}

	private Collection<T> revisit(int numberOfInstances) {
		try {
			List<T> instances = new ArrayList<T>(numberOfInstances);
			List<Producer<T>> producers = producerFactory.createProducers(type, config, route, numberOfInstances);
			if (producers.size() > 1) {
				for (Future<List<T>> future : executorService.invokeAll(producers)) {
					instances.addAll(future.get());
				}
			} else {
				instances = producers.iterator().next().call();
			}
			return instances;
		} catch (Exception e) {
			throw new BehaimException("Population for instances of type " + type.getName() + " failed", e);
		}
	}
}
