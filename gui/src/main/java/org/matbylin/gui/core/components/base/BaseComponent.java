package org.matbylin.gui.core.components.base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;

public abstract class BaseComponent<T extends BaseComponent<T>> implements Component<T> {

    private final SelenideElement root;

    protected BaseComponent(SelenideElement root) {
        this.root = root;
    }

    @Override
    public SelenideElement getRoot() {
        return root;
    }

    protected SelenideElement $(String selector) {
        return root.$(selector);
    }

    protected ElementsCollection $$(String selector) {
        return root.$$(selector);
    }

    protected SelenideElement $(By locator) {
        return root.$(locator);
    }

    protected ElementsCollection $$(By locator) {
        return root.$$(locator);
    }

    public T shouldBeVisible() {
        root.shouldBe(visible);
        return self();
    }

    public T shouldBeNotVisible() {
        root.shouldBe(disappear);
        return self();
    }
}
