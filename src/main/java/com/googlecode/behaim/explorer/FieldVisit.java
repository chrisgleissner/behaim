package com.googlecode.behaim.explorer;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

class FieldVisit {
	private final Field field;
	private final AtomicInteger numberOfVisits = new AtomicInteger();

	public FieldVisit(Field field) {
		this.field = field;
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
		FieldVisit other = (FieldVisit) obj;
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field == null ? 0 : field.hashCode());
		return result;
	}

	boolean isWelcome(int maximumNumberOfVisits) {
		return numberOfVisits.incrementAndGet() <= maximumNumberOfVisits;
	}
}
