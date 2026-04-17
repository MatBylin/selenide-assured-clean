package org.matbylin.gui.browser;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxCapabilities implements Capabilities {
    @Override
    public MutableCapabilities createCapabilities() {
        var options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-smooth-scrolling");
        options.addArguments("--disable-search-engine-choice-screen");
        return options;
    }
}
