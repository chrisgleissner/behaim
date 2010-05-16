package com.googlecode.behaim.builder.producer;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.builder.seeder.Seeder;
import com.googlecode.behaim.explorer.FieldContext;

public class ProducerFieldContext implements FieldContext {

	private final static Logger logger = LoggerFactory.getLogger(ProducerFieldContext.class);

	private final Field field;
	private final Seeder seeder;

	public ProducerFieldContext(Field field, Seeder seeder) {
		this.field = field;
		this.seeder = seeder;
		field.setAccessible(true);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProducerFieldContext other = (ProducerFieldContext) obj;
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		return true;
	}

	public Field getField() {
		return field;
	}

	public Seeder getSeeder() {
		return seeder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field == null ? 0 : field.hashCode());
		return result;
	}

}
