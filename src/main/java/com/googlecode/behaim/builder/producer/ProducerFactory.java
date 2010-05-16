package com.googlecode.behaim.builder.producer;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.behaim.builder.config.Config;
import com.googlecode.behaim.route.Route;

public class ProducerFactory<T> {

	private static final int MIN_NUMBER_OF_INSTANCES_FOR_CONCURRENT_PRODUCTION = 200;

	public List<Producer<T>> createProducers(Class<T> type, Config config, Route route, int numberOfInstances) {
		List<Producer<T>> producers = new ArrayList<Producer<T>>();

		ProducerVisitorContext producerVisitorContext = new ProducerVisitorContext(config);
		ProducerVisitor visitor = new ProducerVisitor(producerVisitorContext);

		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int numberOfInstancesPerProducer = numberOfInstances;
		if (numberOfInstances > MIN_NUMBER_OF_INSTANCES_FOR_CONCURRENT_PRODUCTION) {
			numberOfInstancesPerProducer = numberOfInstances / availableProcessors;
			for (int i = 0; i < availableProcessors; i++) {
				int instances = numberOfInstancesPerProducer;
				if (i == availableProcessors - 1) {
					instances += numberOfInstances - availableProcessors * numberOfInstancesPerProducer;
				}
				producers.add(new Producer<T>(route, visitor, type, numberOfInstancesPerProducer));
			}
		} else {
			producers.add(new Producer<T>(route, visitor, type, numberOfInstances));
		}
		return producers;
	}

}
