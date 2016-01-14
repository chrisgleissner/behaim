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
package com.github.behaim.builder;

import com.github.behaim.Behaim;
import com.github.behaim.BehaimException;
import com.github.behaim.builder.config.Config;
import com.github.behaim.builder.producer.Producer;
import com.github.behaim.builder.producer.ProducerFactory;
import com.github.behaim.builder.producer.ProducerVisitor;
import com.github.behaim.builder.producer.ProducerVisitorContext;
import com.github.behaim.explorer.Explorer;
import com.github.behaim.route.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
