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
package com.github.behaim.explorer;

public class VisitationResult {

    private final FieldContext fieldContext;
    private final Object value;
    private final boolean visitOfValueRequired;

    public VisitationResult(FieldContext fieldContext, Object value, boolean visitOfValueRequired) {
        super();
        this.fieldContext = fieldContext;
        this.value = value;
        this.visitOfValueRequired = visitOfValueRequired;
    }

    public FieldContext getFieldContext() {
        return fieldContext;
    }

    public Object getValue() {
        return value;
    }

    public boolean isVisitOfValueRequired() {
        return visitOfValueRequired;
    }

    @Override
    public String toString() {
        return FieldUtil.getNameAndTypeFor(fieldContext.getField()) + "=" + value;
    }

}
