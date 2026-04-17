package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Checkbox extends BaseElement<Checkbox> {

    public Checkbox(SelenideElement element) {
        super(element);
    }

    public Checkbox check() {
        log.info("Checking checkbox '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled, clickable);

        if (!getElement().isSelected()) {
            getElement().click();
            waitForReadyState();
        }

        return self();
    }

    public Checkbox uncheck() {
        log.info("Unchecking checkbox '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible, enabled, clickable);

        if (getElement().isSelected()) {
            getElement().click();
            waitForReadyState();
        }

        return self();
    }

    public boolean isChecked() {
        log.info("Returning checked state for checkbox '{}'", getElement());
        getElement().shouldBe(visible);
        return getElement().isSelected();
    }

    public Checkbox shouldBeChecked() {
        log.info("Checking that checkbox '{}' is checked", getElement());
        getElement().shouldBe(visible, selected);
        return self();
    }

    public Checkbox shouldBeUnchecked() {
        log.info("Checking that checkbox '{}' is unchecked", getElement());
        getElement().shouldBe(visible);
        getElement().shouldNotBe(selected);
        return self();
    }
}
