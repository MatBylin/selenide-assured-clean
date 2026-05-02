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

    @Key("api.reqres.url")
    String apiReqresUrl();

    @Key("api.qaplayground.url")
    String apiQaPlaygroundUrl();

    @Key("api.app.token")
    String apiAppToken();

    @Key("ui.app.url")
    String uiAppUrl();
}
