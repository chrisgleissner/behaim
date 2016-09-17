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
package com.github.chrisgleissner.behaim;

import com.github.chrisgleissner.behaim.builder.Builder;
import com.github.chrisgleissner.behaim.builder.config.Config;

import java.util.Collection;

public class Behaim<T> {

    public static <T> T build(Class<T> type) {
        return new Builder<T>(type).build();
    }

    public static <T> Collection<T> build(Class<T> type, int numberOfInstances) {
        return new Builder<T>(type).build(numberOfInstances);
    }

    public static <T> Collection<T> build(Class<T> type, int numberOfInstances, Config config) {
        return new Builder<T>(type, config).build(numberOfInstances);
    }

}
