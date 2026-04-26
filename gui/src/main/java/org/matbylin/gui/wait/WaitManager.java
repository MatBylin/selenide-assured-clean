package org.matbylin.gui.wait;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.WebDriverRunner;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;

@UtilityClass
public class WaitManager {

    private final String SPINNER_SELECTOR = System.getProperty("wait.spinner.selector", ".spinner");

    public void waitForPageReady() {
        waitForPageReady(Configuration.timeout, Configuration.pollingInterval);
    }

    public void waitForPageReady(long timeout, long pollingInterval) {
        var wait = new SelenideWait(WebDriverRunner.getWebDriver(), timeout, pollingInterval);
        waitForDocumentReadyState(wait);
        waitForSpinnerDisappear(wait);
    }

    private void waitForDocumentReadyState(SelenideWait wait) {
        wait.until(driver -> Objects.equals(
                ((JavascriptExecutor) driver).executeScript("return document.readyState"), "complete"));
    }

    private void waitForAngularReadyState(SelenideWait wait) {
        wait.until(driver -> {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "return window.getAllAngularTestabilities && " +
                    "window.getAllAngularTestabilities().every(t => t.isStable())");
            return Boolean.TRUE.equals(result);
        });
    }

    private void waitForSpinnerDisappear(SelenideWait wait) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(SPINNER_SELECTOR)));
    }
}
