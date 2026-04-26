package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

@Slf4j
public class RadioGroup extends BaseElement<RadioGroup> {

    public RadioGroup(SelenideElement element) {
        super(element);
    }

    public RadioGroup select(String option) {
        log.info("Selecting option '{}' in radio group '{}'", option, getElement());
        waitForReadyState();
        getElement().shouldBe(visible);
        var radio = getElement().$(byText(option)).shouldBe(visible);
        if (!radio.isSelected()) {
            radio.click();
            waitForReadyState();
        }

        return self();
    }

    public boolean isSelected(String option) {
        log.info("Checking selected option '{}' in radio group '{}'", option, getElement());
        return getElement().$(byText(option)).shouldBe(visible).isSelected();
    }

    public RadioGroup shouldBeSelected(String option) {
        log.info("Checking that option '{}' is selected in radio group '{}'", option, getElement());
        getElement().$(byText(option)).shouldBe(visible, selected);
        return self();
    }

    public RadioGroup shouldNotBeSelected(String option) {
        log.info("Checking that option '{}' is not selected in radio group '{}'", option, getElement());
        getElement().$(byText(option)).shouldBe(visible).shouldNotBe(selected);
        return self();
    }
}
