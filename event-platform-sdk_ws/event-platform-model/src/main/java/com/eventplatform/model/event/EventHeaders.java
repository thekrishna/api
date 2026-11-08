package com.eventplatform.model.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public final class EventHeaders implements Serializable {

    private final Map<String, String> values;

    private EventHeaders(Map<String, String> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(normalize(values)));
    }

    public static Builder builder() {
        return new Builder();
    }

    public String get(String key) {
        return values.get(normalizeKey(key));
    }

    public boolean contains(String key) {
        return values.containsKey(normalizeKey(key));
    }

    public Map<String, String> toMap() {
        return values;
    }

    private static Map<String, String> normalize(Map<String, String> source) {
        Map<String, String> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : source.entrySet()) {
            String key = normalizeKey(entry.getKey());
            normalized.put(key, Objects.requireNonNull(entry.getValue(), "header value must not be null for key " + key));
        }
        return normalized;
    }

    private static String normalizeKey(String key) {
        Objects.requireNonNull(key, "header key must not be null");
        if (key.isBlank()) {
            throw new IllegalArgumentException("header key must not be blank");
        }
        return key.trim().toLowerCase(Locale.ROOT);
    }

    public static final class Builder {
        private final Map<String, String> values = new LinkedHashMap<>();

        public Builder put(String key, String value) {
            values.put(normalizeKey(key), Objects.requireNonNull(value, "header value must not be null for key " + key));
            return this;
        }

        public EventHeaders build() {
            return new EventHeaders(values);
        }
    }
}
