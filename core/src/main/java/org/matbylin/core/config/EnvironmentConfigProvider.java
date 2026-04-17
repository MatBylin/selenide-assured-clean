package org.matbylin.core.config;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;

import java.util.Map;

@UtilityClass
public class EnvironmentConfigProvider {

    private static final String ENV_PROPERTY = "env";
    private static final EnvironmentConfig CONFIG = ConfigFactory.create(EnvironmentConfig.class, Map.of(ENV_PROPERTY, getEnvironment()));

    public static EnvironmentConfig get() {
        return CONFIG;
    }

    private static Environment getEnvironment() {
        return Environment.from(System.getProperty(ENV_PROPERTY));
    }
}
