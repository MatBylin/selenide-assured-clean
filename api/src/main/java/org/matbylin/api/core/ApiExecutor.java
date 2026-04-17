package org.matbylin.api.core;


public interface ApiExecutor {
    <T> ApiResponse<T> get(ApiRequest request, Class<T> responseType);

    <T> ApiResponse<T> post(ApiRequest request, Class<T> responseType);

    <T> ApiResponse<T> put(ApiRequest request, Class<T> responseType);

    <T> ApiResponse<T> delete(ApiRequest request, Class<T> responseType);
}
