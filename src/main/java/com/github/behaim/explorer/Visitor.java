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

import java.lang.reflect.Field;

public interface Visitor {

    /**
     * Visits a {@code field} of the specified {@code object}.
     *
     * @param object to be visited
     * @param field to be visited
     * @return object with which the specified {@code Field} was filled or {@code null} if the field wasn't filled
     */
    VisitationResult visit(Object object, Field field);
}
