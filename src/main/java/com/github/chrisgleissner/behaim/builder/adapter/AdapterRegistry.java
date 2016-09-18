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
package com.github.chrisgleissner.behaim.builder.adapter;

import com.github.chrisgleissner.behaim.BehaimException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterRegistry {
    private final static Class[] SEED_ADAPTER_CLASSES = new Class[]{BigDecimalAdapter.class, BigIntegerAdapter.class,
            BooleanAdapter.class, DateAdapter.class, DoubleAdapter.class, FloatAdapter.class, IntegerAdapter.class,
            LongAdapter.class, StringAdapter.class};

    public Collection<SeedAdapter> getSeedAdapters() {
        List<SeedAdapter> seedAdapters = new ArrayList<SeedAdapter>();
        for (Class seedAdapterClass : SEED_ADAPTER_CLASSES) {
            try {
                seedAdapters.add((SeedAdapter) seedAdapterClass.newInstance());
            } catch (Exception e) {
                throw new BehaimException("Could not instantiate seed adapter " + seedAdapterClass, e);
            }
        }
        return seedAdapters;
    }
}
