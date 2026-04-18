package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Select extends BaseElement<Select> {

    public Select(SelenideElement element) {
        super(element);
    }

    public Select selectByText(String optionText) {
        log.info("Selecting option by text '{}' in '{}'", optionText, getElement());
        waitForReadyState();
        getSelectElement().selectOption(optionText);
        waitForReadyState();
        return self();
    }

    public String getSelectedText() {
        log.info("Returning selected text from '{}'", getElement());
        var select = getSelectElement();
        select.shouldBe(visible);
        return select.getSelectedOptionText();
    }

    public Select shouldHaveSelectedText(String expectedText) {
        log.info("Checking that '{}' has selected text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible);
        getSelectElement().shouldBe(visible).shouldHave(selectedText(expectedText));
        return self();
    }

    private SelenideElement getSelectElement() {
        var element = getElement().shouldBe(visible, enabled);
        if ("select".equalsIgnoreCase(element.getTagName())) {
            return element;
        }
        return element.$x(".//following-sibling::select[1]").shouldBe(visible, enabled);
    }
}
