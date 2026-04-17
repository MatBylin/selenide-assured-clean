package org.matbylin.api.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.matbylin.core.config.EnvironmentConfigProvider;

@Getter
@AllArgsConstructor
public enum TargetApi {
    REQRES(EnvironmentConfigProvider.get().apiAppUrl());

    private final String url;
}
