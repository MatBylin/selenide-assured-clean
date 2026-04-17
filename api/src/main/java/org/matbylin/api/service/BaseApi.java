package org.matbylin.api.service;

import org.matbylin.api.config.auth.AuthProvider;
import org.matbylin.api.core.RestExecutor;

public class BaseApi {
    private final AuthProvider authProvider;
    protected final RestExecutor restExecutor;

    public BaseApi(AuthProvider authProvider) {
        this.authProvider = authProvider;
        this.restExecutor = new RestExecutor(authProvider);
    }
}
