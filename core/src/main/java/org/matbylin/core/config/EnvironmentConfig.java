package org.matbylin.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:environment/${env}.properties",
})
public interface EnvironmentConfig extends Config {

    @Key("environment")
    String env();

    @Key("api.app.url")
    String apiAppUrl();

    @Key("ui.app.url")
    String uiAppUrl();
}
