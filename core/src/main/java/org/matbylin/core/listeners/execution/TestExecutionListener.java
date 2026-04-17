package org.matbylin.core.listeners.execution;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Slf4j
public class TestExecutionListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        log.info("TEST START: {}", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST END: {} -> PASSED", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info("TEST END: {} -> FAILED", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("TEST END: {} -> SKIPPED", result.getMethod().getQualifiedName());
    }
}
