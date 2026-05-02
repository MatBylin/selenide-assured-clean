package org.matbylin.api.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:restassured.properties")
public interface RestAssuredProperties extends Config {

    @Key("http.connection.timeout")
    int connectionTimeout();

    @Key("http.socket.timeout")
    int socketTimeout();
}
