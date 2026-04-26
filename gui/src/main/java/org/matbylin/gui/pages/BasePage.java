package org.matbylin.gui.pages;

import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.wait.WaitManager;

import static com.codeborne.selenide.Selenide.open;

@Slf4j
public abstract class BasePage<T extends BasePage<T>> {
    protected abstract Text getPageTitle();

    protected abstract void validateLoaded();

    public T shouldBeLoaded() {
        log.info("Validating page loaded: {}", this.getClass().getSimpleName());
        WaitManager.waitForPageReady();
        validateLoaded();
        return self();
    }

    public void shouldHaveTitle(String expectedTitle) {
        log.info("Checking that open page has a page title '{}'", expectedTitle);
        getPageTitle().shouldHaveExactText(expectedTitle);
    }

    public T openPage(String url) {
        open(url);
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
}
