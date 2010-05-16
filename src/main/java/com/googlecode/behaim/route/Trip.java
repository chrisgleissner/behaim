package com.googlecode.behaim.route;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.explorer.VisitationResult;
import com.googlecode.behaim.explorer.Visitor;

/**
 * Trip performed according to a {@link Route}.
 * 
 * @author Christian Gleissner
 */
public class Trip {

	private final static Logger logger = LoggerFactory.getLogger(Trip.class);
	private final Iterator<Leg> legIterator;
	private final Object startingLocation;
	private final Visitor visitor;

	public Trip(Route route, Object startingLocation, Visitor visitor) {
		this.startingLocation = startingLocation;
		this.visitor = visitor;
		legIterator = route.getLegs().iterator();
	}

	public void perform() {
		visit(startingLocation);
	}

	private void visit(Object location) {
		while (legIterator.hasNext()) {
			Leg leg = legIterator.next();
			logger.trace("Visiting {} on {}", leg, location);
			switch (leg.getType()) {
			case ITERATE_OVER_ARRAY:
				for (Object value : (Object[]) location) {
					visit(value);
				}
				break;
			case ITERATE_OVER_COLLECTION:
				for (Object value : (Collection) location) {
					visit(value);
				}
				break;
			case RECURSE:
				Field field = leg.getFieldContext().getField();
				VisitationResult result = visitor.visit(location, field);
				visit(result.getValue());
				break;
			case NORMAL:
				field = leg.getFieldContext().getField();
				visitor.visit(location, field);
				continue;
			case RETURN:
				return;
			}
		}
	}
}
