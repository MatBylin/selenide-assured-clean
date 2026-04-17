package org.matbylin.core.test;

import lombok.extern.slf4j.Slf4j;
import org.matbylin.core.config.EnvironmentConfigProvider;
import org.matbylin.core.listeners.execution.TestExecutionListener;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Slf4j
@Listeners(TestExecutionListener.class)
public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void beforeAllTests() {
        logEnvironmentVariables();
    }

    private static void logEnvironmentVariables() {
        var config = EnvironmentConfigProvider.get();

        log.info("============ Environment Variables ============");
        log.info("environment : {}", config.env());
        log.info("api.base.url: {}", config.apiAppUrl());
        log.info("ui.base.url: {}", config.uiAppUrl());
        log.info("===============================================\n");
    }
}
