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
package com.github.chrisgleissner.behaim.builder.producer;

import com.github.chrisgleissner.behaim.builder.seeder.Seeder;
import com.github.chrisgleissner.behaim.explorer.FieldContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ProducerFieldContext implements FieldContext {

    private final static Logger logger = LoggerFactory.getLogger(ProducerFieldContext.class);

    private final Field field;
    private final Seeder seeder;

    public ProducerFieldContext(Field field, Seeder seeder) {
        this.field = field;
        this.seeder = seeder;
        field.setAccessible(true);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProducerFieldContext other = (ProducerFieldContext) obj;
        if (field == null) {
            if (other.field != null) {
                return false;
            }
        } else if (!field.equals(other.field)) {
            return false;
        }
        return true;
    }

    public Field getField() {
        return field;
    }

    public Seeder getSeeder() {
        return seeder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (field == null ? 0 : field.hashCode());
        return result;
    }

}
