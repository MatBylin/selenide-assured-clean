package org.matbylin.gui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.core.config.EnvironmentConfigProvider;
import org.matbylin.core.test.BaseTest;
import org.matbylin.gui.browser.BrowserCapabilitiesFactory;
import org.matbylin.gui.pages.qaplayground.dashboard.DashboardPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

import static com.codeborne.selenide.Selenide.open;

@Slf4j
public abstract class BaseGuiTest extends BaseTest {

    protected DashboardPage dashboardPage;

    @BeforeSuite(alwaysRun = true)
    public void setUpFramework() {
        configureWebDriverRuntime();
        setBrowserCapabilities();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpApplicationState(Method method) {
        configureAllureSelenide();
        openApplication();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownApplicationState() {
        log.info("Closing browser");
        Selenide.closeWebDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownAllureSelenide() {
        log.info("Removing Allure listener");
        SelenideLogger.removeAllListeners();
    }

    @Step("Open application")
    private void openApplication() {
        var baseUrl = EnvironmentConfigProvider.get().uiAppUrl();
        log.info("Opening application with baseUrl: {}", baseUrl);
        open(baseUrl);
        dashboardPage = DashboardPage.getInstance();
    }

    private void configureAllureSelenide() {
        log.info("Adding Allure listener");
        SelenideLogger.removeAllListeners();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    private void configureWebDriverRuntime() {
        var remoteUrl = System.getProperty("selenide.remote");

        if (remoteUrl == null || remoteUrl.isBlank()) {
            log.info("Running GUI tests on local WebDriver");
            return;
        }

        Configuration.remote = remoteUrl;
        log.info("Running GUI tests on remote Selenium Grid: {}", remoteUrl);
    }

    private void setBrowserCapabilities() {
        var browser = Configuration.browser;

        if (browser == null || browser.isBlank()) {
            throw new IllegalStateException("Browser not set!");
        }

        Configuration.browserCapabilities = BrowserCapabilitiesFactory.createBrowserCapabilities(browser);
    }
}
