package org.matbylin.api.core;

import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.matbylin.api.config.auth.AuthProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class RestExecutor implements ApiExecutor {

    private final RequestSpecificationBuilder requestSpecificationBuilder;

    public RestExecutor(AuthProvider authProvider) {
        this.requestSpecificationBuilder = new RequestSpecificationBuilder(authProvider);
    }

    @Override
    public <T> ApiResponse<T> get(ApiRequest request, Class<T> responseType) {
        var response = requestSpecificationBuilder.build(request)
                .when()
                .get(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> post(ApiRequest request, Class<T> responseType) {
        var response = requestSpecificationBuilder.build(request)
                .body(request.getBody())
                .when()
                .post(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> put(ApiRequest request, Class<T> responseType) {
        var response = requestSpecificationBuilder.build(request)
                .body(request.getBody())
                .when()
                .put(request.getPath());

        return mapResponse(response, responseType);
    }

    @Override
    public <T> ApiResponse<T> delete(ApiRequest request, Class<T> responseType) {
        var response = requestSpecificationBuilder.build(request)
                .when()
                .delete(request.getPath());

        return mapResponse(response, responseType);
    }

    private <T> ApiResponse<T> mapResponse(Response response, Class<T> responseType) {
        logResponse(response, responseType);
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
        if (shouldNotBeDeserialized(response, responseType)) {
            return null;
        }
        if (responseType == byte[].class) {
            return (T) response.asByteArray();
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

    private boolean shouldNotBeDeserialized(Response response, Class<?> responseType) {
        return responseType == null || responseType == Void.class || responseType == void.class || response.getBody() == null;
    }

    private void logResponse(Response response, Class<?> responseType) {
        var isBinary = responseType == byte[].class;

        var logged = response.then().log().status();
        if (!isBinary) {
            logged.log().body();
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
