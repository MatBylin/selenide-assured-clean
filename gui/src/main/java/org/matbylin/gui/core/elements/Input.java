package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Input extends BaseElement<Input> {

    public Input(SelenideElement element) {
        super(element);
    }

    public Input setValue(String valueToSet) {
        log.info("Setting value '{}' on element '{}'", valueToSet, getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled);
        getElement().setValue(valueToSet);
        waitForReadyState();
        return self();
    }

    public Input setSensitiveValue(String valueToSet) {
        log.info("Setting value '********' (sensitive data) on element '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled);
        getElement().setValue(valueToSet);
        waitForReadyState();
        return self();
    }

    public Input appendValue(String valueToSet) {
        log.info("Appending value '{}' to element '{}'", valueToSet, getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled);
        getElement().append(valueToSet);
        return self();
    }

    public Input clearValue() {
        log.info("Clearing value of element '{}'", getElement());
        getElement().shouldBe(visible);
        getElement().clear();
        waitForReadyState();
        return self();
    }

    public String getValue() {
        log.info("Returning value of element '{}'", getElement());
        getElement().shouldBe(visible);
        return getElement().getValue();
    }

    public Input shouldHaveValue(String expectedValue) {
        log.info("Checking that element '{}' has value '{}'", getElement(), expectedValue);
        getElement().shouldBe(visible);
        getElement().shouldHave(value(expectedValue));
        return self();
    }

    public Input pressEnter() {
        log.info("Pressing enter on element '{}'", getElement());
        getElement().shouldBe(visible);
        getElement().sendKeys(Keys.ENTER);
        waitForReadyState();
        return self();
    }
}
