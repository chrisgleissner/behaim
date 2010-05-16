package com.googlecode.behaim.explorer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.BehaimException;
import com.googlecode.behaim.route.Leg;
import com.googlecode.behaim.route.LegType;
import com.googlecode.behaim.route.Route;

public class Explorer {

	private final static Logger logger = LoggerFactory.getLogger(Explorer.class);
	private final ConcurrentMap<Field, FieldVisit> fieldVisits = new ConcurrentHashMap<Field, FieldVisit>();
	private final int recursionDepth;
	private final Visitor visitor;

	public Explorer(Visitor visitor, int recursionDepth) {
		this.visitor = visitor;
		this.recursionDepth = recursionDepth;
	}

	private boolean complex(Field field) {
		return !field.isEnumConstant() && !field.getType().isPrimitive();
	}

	private boolean descendable(Field field) {
		FieldVisit fieldVisit = fieldVisits.get(field);
		if (fieldVisit == null) {
			fieldVisit = new FieldVisit(field);
			FieldVisit existingFieldVisit = fieldVisits.putIfAbsent(field, fieldVisit);
			if (existingFieldVisit != null) {
				fieldVisit = existingFieldVisit;
			}
		}
		return fieldVisit.isWelcome(recursionDepth);

	}

	private boolean explorable(VisitationResult visitationResult) {
		Field field = visitationResult.getFieldContext().getField();
		return visitationResult.isVisitOfValueRequired() && complex(field) && descendable(field);
	}

	public Route explore(Object object) {
		Route route = new Route();
		explore(route, object, 0);
		return route;
	}

	private void explore(Route route, Object object, int recursionDepth) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (visitable(field)) {
				VisitationResult visitationResult = visitor.visit(object, field);
				logVisit(visitationResult, recursionDepth);
				if (explorable(visitationResult)) {
					route.add(new Leg(visitationResult.getFieldContext(), LegType.RECURSE));
					recursionDepth++;
					try {
						Object fieldValue = visitationResult.getValue();
						if (fieldValue instanceof Collection) {
							route.add(new Leg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_COLLECTION));
							for (Object fieldElement : (Collection) fieldValue) {
								explore(route, fieldElement, recursionDepth);
							}
						} else if (fieldValue.getClass().isArray()) {
							route.add(new Leg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_ARRAY));
							for (Object fieldElement : (Object[]) fieldValue) {
								explore(route, fieldElement, recursionDepth);
							}
						} else {
							explore(route, fieldValue, recursionDepth);
						}
					} catch (Exception e) {
						throw new BehaimException("Could not explore " + field.getDeclaringClass().getName() + "."
						        + field.getName() + " on " + object, e);
					}
					route.add(Leg.RETURN_LEG);
				} else {
					route.add(new Leg(visitationResult.getFieldContext(), LegType.NORMAL));
				}
			}
		}
	}

	private void logVisit(VisitationResult visitationResult, int recursionDepth) {
		if (logger.isTraceEnabled()) {
			int indentationWidth = recursionDepth * 3;
			StringBuilder sb = new StringBuilder(indentationWidth);
			for (int i = 0; i < indentationWidth; i++) {
				sb.append(" ");
			}
			if (logger.isTraceEnabled()) {
				logger.trace("{}[{}] {}", new Object[] { sb, recursionDepth, visitationResult });
			}
		}
	}

	private boolean modifiable(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers);
	}

	private boolean visitable(Field field) {
		return modifiable(field);
	}
}
