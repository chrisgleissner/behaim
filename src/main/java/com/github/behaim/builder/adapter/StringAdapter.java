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
package com.github.behaim.builder.adapter;

import com.github.behaim.builder.config.FieldConfig;
import com.github.behaim.builder.seeder.Seeder;

import java.util.Random;

public class StringAdapter extends AbstractSeedAdapter<String> {

    private final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final Random random = new Random();

    @Override
    public String convert(Seeder seeder) {
        FieldConfig fieldConfig = seeder.getConfig();
        int length = random.nextInt(fieldConfig.getMaxLength() - fieldConfig.getMinLength()) + fieldConfig.getMinLength();
        StringBuilder sb = new StringBuilder(length);
        long seed = (long) seeder.createSeed();
        if (seed < 0) {
            seed *= -1;
        }
        long initialSeed = seed;
        for (int i = 0; i < length; i++) {
            int charIndex = (int) (seed % CHARS.length());
            sb.append(CHARS.charAt(charIndex));
            seed /= CHARS.length();
            if (seed == 0) {
                seed = initialSeed;
            }
        }
        return sb.toString();
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }
}
