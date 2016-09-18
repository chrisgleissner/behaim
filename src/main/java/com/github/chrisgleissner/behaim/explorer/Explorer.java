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
package com.github.chrisgleissner.behaim.explorer;

import com.github.chrisgleissner.behaim.BehaimException;
import com.github.chrisgleissner.behaim.route.LegType;
import com.github.chrisgleissner.behaim.route.Route;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class Explorer {

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
                    route.addLeg(visitationResult.getFieldContext(), LegType.RECURSE);
                    try {
                        Object fieldValue = visitationResult.getValue();
                        if (fieldValue != null) {
                            if (fieldValue instanceof Collection) {
                                route.addLeg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_COLLECTION);
                                int elementIndex = 0;
                                for (Object fieldElement : (Collection) fieldValue) {
                                    explore(route, fieldElement, trace.add(field, elementIndex++));
                                    trace.remove();
                                }
                            } else if (fieldValue.getClass().isArray()) {
                                route.addLeg(visitationResult.getFieldContext(), LegType.ITERATE_OVER_ARRAY);
                                int elementIndex = 0;
                                for (Object fieldElement : (Object[]) fieldValue) {
                                    explore(route, fieldElement, trace.add(field, elementIndex++));
                                    trace.remove();
                                }
                            } else {
                                explore(route, fieldValue, trace);
                            }
                        }
                    } catch (Exception e) {
                        throw new BehaimException("Could not explore " + field.getDeclaringClass().getName() + "."
                                + field.getName() + " on " + object, e);
                    }
                    route.addLeg(null, LegType.RETURN);
                } else {
                    route.addLeg(visitationResult.getFieldContext(), LegType.NORMAL);
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
