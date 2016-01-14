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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Stack;

public class Trace {

    private final static class Breadcrumb {

        private final String name;
        private final Class type;

        public Breadcrumb(Field field) {
            type = field.getType();
            name = field.getName();
        }

        private Breadcrumb(int elementIndex) {
            name = "[" + elementIndex + "]";
            type = null;
        }

        private String getName() {
            return name;
        }

        private Class getType() {
            return type;
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(Trace.class);

    private final Stack<Breadcrumb> breadcrumbs = new Stack<Breadcrumb>();

    public Trace add(Field field) {
        breadcrumbs.push(new Breadcrumb(field));
        logger.trace("+{}", this);
        return this;
    }

    public Trace add(Field field, int elementIndex) {
        breadcrumbs.push(new Breadcrumb(elementIndex));
        logger.trace("+{}", this);
        return this;
    }

    public int getNumberOfOccurrences(Class type) {
        // TODO gleissc Replace this with a Map<Class, ClassCount> to improve performance
        int numberOfOccurrences = 0;
        for (Breadcrumb breadcrumb : breadcrumbs) {
            if (type.equals(breadcrumb.getType())) {
                numberOfOccurrences++;
            }
        }
        return numberOfOccurrences;
    }

    public Breadcrumb remove() {
        Breadcrumb breadcrumb = breadcrumbs.pop();
        logger.trace("-{}", this);
        return breadcrumb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Breadcrumb breadcrumb : breadcrumbs) {
            if (first) {
                first = false;
            } else {
                sb.append("/");
            }
            sb.append(breadcrumb.getName());
        }
        return sb.toString();
    }
}
