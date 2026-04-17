package org.matbylin.api.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class ApiResponse<T> {
    private final int statusCode;
    private final T body;
    private final String rawBody;
    private final Map<String, List<String>> headers;

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}
