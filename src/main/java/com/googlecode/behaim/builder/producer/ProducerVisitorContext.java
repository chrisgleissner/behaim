package com.googlecode.behaim.builder.producer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.googlecode.behaim.builder.adapter.AdapterRegistry;
import com.googlecode.behaim.builder.adapter.SeedAdapter;
import com.googlecode.behaim.builder.config.Config;
import com.googlecode.behaim.builder.config.FieldConfig;
import com.googlecode.behaim.builder.seeder.Seeder;
import com.googlecode.behaim.builder.seeder.SeederFactory;
import com.googlecode.behaim.explorer.FieldUtil;

public class ProducerVisitorContext {

	private final Config config;
	private final ConcurrentMap<Field, ProducerFieldContext> producerFieldContexts = new ConcurrentHashMap<Field, ProducerFieldContext>();
	@SuppressWarnings("unchecked")
	private final Map<Class, SeedAdapter> seedAdapters = new HashMap<Class, SeedAdapter>();
	private final SeederFactory seederFactory = new SeederFactory();

	public ProducerVisitorContext() {
		this(Config.DEFAULT_CONFIG);
	}

	public ProducerVisitorContext(Config config) {
		this.config = config;
		AdapterRegistry valueFactoryRegistry = new AdapterRegistry();
		for (SeedAdapter<?> seedAdapter : valueFactoryRegistry.getSeedAdapters()) {
			seedAdapters.put(seedAdapter.getValueClass(), seedAdapter);
		}
	}

	public Config getConfig() {
		return config;
	}

	public ProducerFieldContext getFieldContextFor(Field field) {
		ProducerFieldContext producerFieldContext = producerFieldContexts.get(field);
		if (producerFieldContext == null) {
			FieldConfig fieldConfig = config.getFieldConfigFor(FieldUtil.getNameFor(field));
			Seeder seeder = seederFactory.createSeeder(fieldConfig);
			producerFieldContext = new ProducerFieldContext(field, seeder);
			ProducerFieldContext existingFieldContext = producerFieldContexts.putIfAbsent(field, producerFieldContext);
			if (existingFieldContext != null) {
				producerFieldContext = existingFieldContext;
			}
		}
		return producerFieldContext;
	}

	public SeedAdapter<?> getSeedAdapterFor(Field field) {
		Class<?> type = field.getType();
		if (type.isPrimitive()) {
			type = TypeUtil.wrap(type);
		}
		return seedAdapters.get(type);
	}
}
