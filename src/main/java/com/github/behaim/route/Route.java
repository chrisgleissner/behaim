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
package com.github.behaim.route;

import com.github.behaim.explorer.Explorer;
import com.github.behaim.explorer.FieldContext;
import com.github.behaim.explorer.Visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Route discovered by an {@link Explorer}.
 *
 * @author Christian Gleissner
 * @author Fabien Duminy
 */
public class Route {

    private final List<Leg> legs = new ArrayList<Leg>();

    public void addLeg(FieldContext fieldContext, LegType legType) {
        if (LegType.RETURN.equals(legType)) {
            legs.add(Leg.RETURN_LEG);
        } else {
            // following code tries to avoid creating a new instance of Leg when it's not necessary.
            for (int i = 0; i < legs.size(); i++) {
                Leg leg = legs.get(i);
                if (Objects.equals(leg.getType(), legType) && Objects.equals(leg.getFieldContext(), fieldContext)) {
                    legs.add(leg);
                    return;
                }
            }
            legs.add(new Leg(fieldContext, legType));
        }
    }

    private void append(StringBuilder sb, String s, int recursionLevel) {
        for (int i = 0; i < recursionLevel; i++) {
            sb.append("  ");
        }
        sb.append(s);
        sb.append("\n");
    }

    public Collection<Leg> getLegs() {
        return legs;
    }

    public Trip prepareTrip(Object startingLocation, Visitor visitor) {
        return new Trip(this, startingLocation, visitor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(4096);
        int recursionLevel = 0;
        for (Leg leg : legs) {
            switch (leg.getType()) {
                case ITERATE_OVER_ARRAY:
                    append(sb, "[]", recursionLevel);
                    break;
                case ITERATE_OVER_COLLECTION:
                    append(sb, "collection", recursionLevel);
                    break;
                case RECURSE:
                    recursionLevel++;
                    append(sb, leg.toString(), recursionLevel);
                    break;
                case NORMAL:
                    append(sb, leg.toString(), recursionLevel);
                    break;
                case RETURN:
                    recursionLevel--;
                    break;
            }
        }

        return sb.toString();
    }
}
