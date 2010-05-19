package com.googlecode.behaim.explorer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.BehaimException;
import com.googlecode.behaim.route.Leg;
import com.googlecode.behaim.route.LegType;
import com.googlecode.behaim.route.Route;

public class Explorer {

	private final static Logger logger = LoggerFactory.getLogger(Explorer.class);
	private final int recursionDepth;
	private final Visitor visitor;

	public Explorer(Visitor visitor, int recursionDepth) {
		this.visitor = visitor;
		this.recursionDepth = recursionDepth;
	}

	public Route explore(Object object) {
		Route route = new Route();
		Trace trace = new Trace();
		explore(route, object, trace);
		return route;
	}

	private void explore(Route route, Object object, Trace trace) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (visitable(field)) {
				trace.add(field);
				VisitationResult visitationResult = visitor.visit(object, field);
				if (isExplorable(visitationResult, trace)) {
					route.add(new Leg(visitationResult.getFieldContext(), LegType.RECURSE));
					try {
						Object fieldValue = visitationResult.getValue();
						if (fieldValue instanceof Collection) {
							route.add(new Leg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_COLLECTION));
							int elementIndex = 0;
							for (Object fieldElement : (Collection) fieldValue) {
								explore(route, fieldElement, trace.add(field, elementIndex++));
								trace.remove();
							}
						} else if (fieldValue.getClass().isArray()) {
							route.add(new Leg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_ARRAY));
							int elementIndex = 0;
							for (Object fieldElement : (Object[]) fieldValue) {
								explore(route, fieldElement, trace.add(field, elementIndex++));
								trace.remove();
							}
						} else {
							explore(route, fieldValue, trace);
						}
					} catch (Exception e) {
						throw new BehaimException("Could not explore " + field.getDeclaringClass().getName() + "."
						        + field.getName() + " on " + object, e);
					}
					route.add(Leg.RETURN_LEG);
				} else {
					route.add(new Leg(visitationResult.getFieldContext(), LegType.NORMAL));
				}
				trace.remove();
			}
		}
	}

	private boolean hasComplexType(Field field) {
		return !field.isEnumConstant() && !field.getType().isPrimitive();
	}

	private boolean isDescendable(Field field, Trace trace) {
		return trace.getNumberOfOccurrences(field.getType()) < recursionDepth;
	}

	private boolean isExplorable(VisitationResult visitationResult, Trace trace) {
		Field field = visitationResult.getFieldContext().getField();
		return visitationResult.isVisitOfValueRequired() && hasComplexType(field) && isDescendable(field, trace);
	}

	private boolean modifiable(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers);
	}

	private boolean visitable(Field field) {
		return modifiable(field);
	}
}
