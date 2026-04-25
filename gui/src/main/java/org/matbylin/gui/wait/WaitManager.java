package org.matbylin.gui.wait;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;

public class WaitManager {

    private static final By SPINNER = By.cssSelector(
            System.getProperty("wait.spinner.selector", ".spinner")
    );

    private final SelenideWait wait;

    public WaitManager() {
        this.wait = new SelenideWait(WebDriverRunner.getWebDriver(), Configuration.timeout, Configuration.pollingInterval);
    }

    public WaitManager(long timeout, long pollingInterval) {
        this.wait = new SelenideWait(WebDriverRunner.getWebDriver(), timeout, pollingInterval);
    }

    public void waitForPageReady() {
        waitForDocumentReady();
        waitForSpinnerDisappear();
    }

    private void waitForDocumentReady() {
        wait.until(driver -> Objects.equals(((JavascriptExecutor) driver)
                .executeScript("return document.readyState"), "complete")
        );
    }

    private void waitForSpinnerDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SPINNER));
    }
}
