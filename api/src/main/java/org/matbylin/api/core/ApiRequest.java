package org.matbylin.api.core;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.matbylin.api.config.TargetApi;

import java.util.Map;

@Getter
@Builder(toBuilder = true)
public class ApiRequest {
    private final TargetApi targetApi;
    private final String path;
    @Singular("header")
    private final Map<String, String> headers;
    @Singular("queryParam")
    private final Map<String, Object> queryParams;
    @Singular("pathParam")
    private final Map<String, Object> pathParams;
    private final Object body;
}
