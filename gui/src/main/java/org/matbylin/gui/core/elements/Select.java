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
        log.info("Selecting option by text '{}' in select '{}'", optionText, getElement());
        waitForReadyState();
        getSelectElement().selectOption(optionText);
        return self();
    }

    public String getSelectedText() {
        log.info("Returning selected text from select '{}'", getElement());
        waitForReadyState();
        return getSelectElement().getSelectedOptionText();
    }

    public Select shouldHaveSelectedText(String expectedText) {
        log.info("Checking that select '{}' has selected text '{}'", getElement(), expectedText);
        getSelectElement().shouldHave(selectedText(expectedText));
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
