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
        log.info("Setting value '{}' in input '{}'", valueToSet, getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled).setValue(valueToSet);
        return self();
    }

    public Input setSensitiveValue(String valueToSet) {
        log.info("Setting value '********' (sensitive data) in input '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled).setValue(valueToSet);
        return self();
    }

    public Input appendValue(String valueToSet) {
        log.info("Appending value '{}' to input '{}'", valueToSet, getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled).append(valueToSet);
        return self();
    }

    public Input clearValue() {
        log.info("Clearing value of input '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible).clear();
        return self();
    }

    public String getValue() {
        log.info("Returning value of input '{}'", getElement());
        waitForReadyState();
        return getElement().shouldBe(visible).getValue();
    }

    public Input shouldHaveValue(String expectedValue) {
        log.info("Checking that input '{}' has value '{}'", getElement(), expectedValue);
        getElement().shouldBe(visible).shouldHave(value(expectedValue));
        return self();
    }

    public Input pressEnter() {
        log.info("Pressing enter on input '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible).sendKeys(Keys.ENTER);
        return self();
    }
}
