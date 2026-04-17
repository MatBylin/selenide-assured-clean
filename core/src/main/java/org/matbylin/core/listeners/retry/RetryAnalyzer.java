package org.matbylin.core.listeners.retry;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRIES = resolveMaxRetries();

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount >= MAX_RETRIES) {
            return false;
        }

        retryCount++;
        log.warn("Retrying test {}. Attempt {}/{}", result.getMethod().getQualifiedName(), retryCount, MAX_RETRIES);
        return true;
    }

    private static int resolveMaxRetries() {
        var configuredValue = System.getProperty("retry.count", "0");
        try {
            return Math.max(Integer.parseInt(configuredValue), 0);
        } catch (NumberFormatException exception) {
            log.warn("Invalid 'retryCount' value '{}'. Falling back to no retry.", configuredValue);
            return 0;
        }
    }
}
