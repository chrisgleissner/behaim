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
package com.github.chrisgleissner.behaim.route;

import com.github.chrisgleissner.behaim.explorer.VisitationResult;
import com.github.chrisgleissner.behaim.explorer.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

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
                    logger.trace("{}={}", leg, result.getValue());
                    visit(result.getValue());
                    break;
                case NORMAL:
                    field = leg.getFieldContext().getField();
                    result = visitor.visit(location, field);
                    logger.trace("{}={}", leg, result.getValue());
                    continue;
                case RETURN:
                    return;
            }
        }
    }
}
