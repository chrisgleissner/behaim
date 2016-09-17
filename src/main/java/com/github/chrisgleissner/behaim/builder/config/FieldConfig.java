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
package com.github.chrisgleissner.behaim.builder.config;

public class FieldConfig {

    private final int maxLength;
    private final int maxValue;
    private final int minLength;
    private final int minValue;
    private final boolean random;
    // TODO gleissc Add support for unique random values, then expose this field
    private final boolean unique = false;

    public FieldConfig() {
        this(true, 0, Integer.MAX_VALUE, 3, 10);
    }

    public FieldConfig(boolean random, int minValue, int maxValue, int minLength, int maxLength) {
        this.random = random;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMinValue() {
        return minValue;
    }

    public boolean isConstantValue() {
        return minValue == maxValue;
    }

    public boolean isRandom() {
        return random;
    }

    public boolean isUnique() {
        return unique;
    }
}
