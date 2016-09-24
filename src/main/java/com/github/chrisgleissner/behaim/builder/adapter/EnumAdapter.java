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

import com.github.chrisgleissner.behaim.builder.seeder.Seeder;

/**
 * @author Fabien DUMINY
 */
public class EnumAdapter<E extends Enum<E>> extends AbstractSeedAdapter<E> {
    private final Class<E> enumClass;

    public EnumAdapter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E convert(Seeder seeder) {
        E[] enumValues = enumClass.getEnumConstants();
        if (enumValues.length == 0) {
            return null;
        }
        if (enumValues.length == 1) {
            return enumValues[0];
        }

        return enumValues[seeder.createIntSeed()];
    }

    @Override
    public Class<E> getValueClass() {
        return enumClass;
    }

    public static <E extends Enum<E>> SeedAdapter<E> forClass(Class<E> enumClass) {
        return new EnumAdapter<E>(enumClass);
    }
}