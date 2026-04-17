package org.matbylin.gui.browser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.MutableCapabilities;

import java.util.Map;
import java.util.Objects;


@Slf4j
public class BrowserCapabilitiesFactory {
    private static final Map<BrowserType, Capabilities> CAPABILITIES = Map.of(
            BrowserType.CHROME, new ChromeCapabilities(),
            BrowserType.FIREFOX, new FirefoxCapabilities()
    );

    public static MutableCapabilities createBrowserCapabilities(String browserType) {
        log.info("Creating browser capabilities for: {}", browserType);
        var capabilities = CAPABILITIES.get(BrowserType.valueOf(browserType.toUpperCase()));

        if (Objects.isNull(capabilities)) {
            var errorMessage = "Browser: '%s' is not supported!".formatted(browserType);
            log.error(errorMessage);
            throw new NotImplementedException(errorMessage);
        }

        return capabilities.createCapabilities();
    }
}