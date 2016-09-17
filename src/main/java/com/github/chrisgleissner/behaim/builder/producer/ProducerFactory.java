/*
 * Copyright (C) 2010-2016 Christian Gleissner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.chrisgleissner.behaim.builder.producer;

import com.github.chrisgleissner.behaim.builder.config.Config;
import com.github.chrisgleissner.behaim.route.Route;

import java.util.ArrayList;
import java.util.List;

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
