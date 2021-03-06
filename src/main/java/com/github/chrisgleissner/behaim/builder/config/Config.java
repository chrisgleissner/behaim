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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static final int DEFAULT_BATCH_SIZE = 1;
    private static final int DEFAULT_RECURSION_DEPTH = 2;

    public final static Config DEFAULT_CONFIG = new Config();

    private final int batchSize;
    private final int recursionDepth;

    private final List<String> excludedClassNames = new ArrayList<String>();
    private final Map<String, FieldConfig> fieldConfigs;
    private final FieldConfig globalFieldConfig = new FieldConfig();

    public Config() {
        this(DEFAULT_BATCH_SIZE, DEFAULT_RECURSION_DEPTH, null);
    }

    public Config(int batchSize) {
        this(batchSize, DEFAULT_RECURSION_DEPTH, null);
    }

    public Config(int batchSize, int recursionDepth) {
        this(batchSize, recursionDepth, null);
    }

    public Config(int batchSize, int recursionDepth, Map<String, FieldConfig> fieldConfigs) {
        this.batchSize = batchSize;
        this.recursionDepth = recursionDepth;
        this.fieldConfigs = (fieldConfigs != null) ? fieldConfigs : new HashMap<String, FieldConfig>();
        excludedClassNames.add("java");
    }

    public int getBatchSize() {
        return batchSize;
    }

    public List<String> getExcludedClassNames() {
        return excludedClassNames;
    }

    public FieldConfig getFieldConfigFor(String fieldName) {
        FieldConfig fieldConfig = fieldConfigs.get(fieldName);
        if (fieldConfig == null) {
            fieldConfig = globalFieldConfig;
        }
        return fieldConfig;
    }

    public Map<String, FieldConfig> getFieldConfigs() {
        return fieldConfigs;
    }

    public int getRecursionDepth() {
        return recursionDepth;
    }
}
