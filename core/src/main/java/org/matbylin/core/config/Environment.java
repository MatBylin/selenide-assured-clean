package org.matbylin.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Environment {
    DEV("dev"),
    TEST("test"),
    STAGE("stage");

    private final String value;

    public static Environment from(String value) {
        Objects.requireNonNull(value, "Environment value cannot be null !!!");
        String normalizedValue = value.toLowerCase().trim();
        for (Environment environment : values()) {
            if (environment.getValue().equals(normalizedValue)) {
                return environment;
            }
        }
        throw new IllegalArgumentException(
                "Provided environment: '%s' is not supported! Supported values: %s".formatted(value, Arrays.toString(values()))
        );
    }
}
