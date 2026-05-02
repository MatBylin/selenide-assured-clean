package org.matbylin.api.core;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.matbylin.api.config.RestAssuredPropertiesProvider;
import org.matbylin.api.config.auth.AuthProvider;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.protocol.HTTP.CONTENT_TYPE;

public class RequestSpecificationBuilder {

    private static final String X_API_KEY = "x-api-key";
    private static final String HTTP_CONNECTION_TIMEOUT = "http.connection.timeout";
    private static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";

    private final AuthProvider authProvider;

    public RequestSpecificationBuilder(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public RequestSpecification build(ApiRequest request) {
        return RestAssured.given()
                .config(timeoutConfig())
                .baseUri(request.getTargetApi().getUrl())
                .headers(request.getHeaders())
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .header(X_API_KEY, authProvider.getToken())
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .log().uri()
                .log().method()
                .log().body();
    }

    private RestAssuredConfig timeoutConfig() {
        var restAssuredProperties = RestAssuredPropertiesProvider.get();
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(HTTP_CONNECTION_TIMEOUT, restAssuredProperties.connectionTimeout())
                        .setParam(HTTP_SOCKET_TIMEOUT, restAssuredProperties.socketTimeout()));
    }
}
