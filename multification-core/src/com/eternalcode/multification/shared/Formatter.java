/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eternalcode.multification.shared;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Formatter {

    private final Map<String, Supplier<?>> placeholders = new LinkedHashMap<>();

    public String format(String message) {
        for (Map.Entry<String, Supplier<?>> placeholderEntry : placeholders.entrySet()) {
            String key = placeholderEntry.getKey();

            if (!message.contains(key)) {
                continue;
            }

            Object value = placeholderEntry.getValue().get();

            if (value == null) {
                continue;
            }

            message = message.replace(key, value.toString());
        }

        return message;
    }

    public Formatter register(String placeholder, Object value) {
        return register(placeholder, value::toString);
    }

    public Formatter register(String placeholder, Supplier<?> value) {
        this.placeholders.put(placeholder, value);
        return this;
    }

}
