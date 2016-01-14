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

import com.github.behaim.explorer.FieldContext;
import com.github.behaim.explorer.FieldUtil;

/**
 * Leg of a {@link Route}.
 *
 * @author Christian Gleissner
 */
public class Leg {

    public final static Leg RETURN_LEG = new Leg(null, LegType.RETURN);
    private final FieldContext fieldContext;
    private final LegType type;

    public Leg(FieldContext fieldContext, LegType legType) {
        this.fieldContext = fieldContext;
        type = legType;
    }

    public FieldContext getFieldContext() {
        return fieldContext;
    }

    public LegType getType() {
        return type;
    }

    @Override
    public String toString() {
        String s;
        if (fieldContext == null) {
            s = type.name();
        } else {
            s = type.name() + "/" + FieldUtil.getNameAndTypeFor(fieldContext.getField());
        }
        return s;
    }
}
