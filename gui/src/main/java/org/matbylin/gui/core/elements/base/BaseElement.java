package org.matbylin.gui.core.elements.base;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import org.matbylin.gui.wait.WaitManager;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;

public abstract class BaseElement<T extends BaseElement<T>> implements Element<T> {

    private final SelenideElement element;

    protected BaseElement(SelenideElement element) {
        this.element = element;
    }

    @Override
    public SelenideElement getElement() {
        return element;
    }

    public T shouldExist() {
        element.should(exist);
        return self();
    }

    public T shouldBeVisible() {
        element.shouldBe(visible);
        return self();
    }

    public T shouldHave(WebElementCondition... conditions) {
        element.shouldHave(conditions);
        return self();
    }

    public T shouldBe(WebElementCondition... conditions) {
        element.shouldBe(conditions);
        return self();
    }

    public T shouldHave(Duration timeout, WebElementCondition... conditions) {
        for (WebElementCondition condition : conditions) {
            element.shouldHave(condition, timeout);
        }
        return self();
    }

    public T shouldBe(Duration timeout, WebElementCondition... conditions) {
        for (WebElementCondition condition : conditions) {
            element.shouldBe(condition, timeout);
        }
        return self();
    }

    public void waitForReadyState() {
        WaitManager.waitForPageReady();
    }
}
