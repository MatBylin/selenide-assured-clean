package org.matbylin.api.config;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigFactory;


@UtilityClass
public class RestAssuredPropertiesProvider {

    private static final RestAssuredProperties CONFIG = ConfigFactory.create(RestAssuredProperties.class);

    public static RestAssuredProperties get() {
        return CONFIG;
    }
}
