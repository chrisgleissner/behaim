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

public class FieldUtil {
    public static String getNameAndTypeFor(Field field) {
        return getNameFor(field) + ":" + getTypeNameFor(field);
    }

    public static String getNameFor(Field field) {
        return field.getDeclaringClass().getSimpleName() + "." + field.getName();
    }

    public static String getTypeNameFor(Field field) {
        return field.getType().getName();
    }
}
