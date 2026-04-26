package org.matbylin.api.core;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.matbylin.api.config.auth.AuthProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
public class RestExecutor implements ApiExecutor {

    private final AuthProvider authProvider;

    @Override
    public <T> ApiResponse<T> get(ApiRequest request, Class<T> responseType) {
        var response = givenRequest(request)
                .when()
                .get(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> post(ApiRequest request, Class<T> responseType) {
        var response = givenRequest(request)
                .body(request.getBody())
                .when()
                .post(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> put(ApiRequest request, Class<T> responseType) {
        var response = givenRequest(request)
                .body(request.getBody())
                .when()
                .put(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> delete(ApiRequest request, Class<T> responseType) {
        var response = givenRequest(request)
                .when()
                .delete(request.getPath());

        return mapResponse(response, responseType);
    }

    private RequestSpecification givenRequest(ApiRequest request) {
        return RestAssured.given()
                .baseUri(request.getTargetApi().getUrl())
                .headers(request.getHeaders())
                .queryParams(request.getQueryParams())
                .pathParams(request.getPathParams())
                .header("x-api-key", authProvider.getToken())
                .header("Content-Type", "application/json")
                .log().uri()
                .log().method()
                .log().body();
    }

    private <T> ApiResponse<T> mapResponse(Response response, Class<T> responseType) {
        response.then()
                .log().status()
                .log().body();

        var rawBody = rawBody(response);
        var deserializedBody = deserialize(response, responseType);

        return new ApiResponse<>(
                response.getStatusCode(),
                deserializedBody,
                rawBody,
                headerMap(response)
        );
    }

    private static String rawBody(Response response) {
        var body = response.getBody();
        return body == null ? StringUtils.EMPTY : body.asString();
    }

    private <T> T deserialize(Response response, Class<T> responseType) {
        if (responseType == null || responseType == Void.class || responseType == void.class) {
            return null;
        }
        if (response.getBody() == null) {
            return null;
        }
        try {
            return response.as(responseType);
        } catch (Exception e) {
            log.warn(
                    "Failed to deserialize body to {} (HTTP {}): {}",
                    responseType.getSimpleName(),
                    response.getStatusCode(),
                    e.getMessage()
            );
            return null;
        }
    }

    private static Map<String, List<String>> headerMap(Response response) {
        return response.getHeaders().asList().stream()
                .collect(Collectors.groupingBy(
                        Header::getName,
                        Collectors.mapping(Header::getValue, Collectors.toList())
                ));
    }
}
