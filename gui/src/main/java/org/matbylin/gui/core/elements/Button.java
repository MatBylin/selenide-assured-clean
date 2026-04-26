package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Button extends BaseElement<Button> {

    public Button(SelenideElement element) {
        super(element);
    }

    public Button click() {
        log.info("Clicking into button '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(clickable).click();
        return self();
    }

    public String getText() {
        log.info("Returning text from button '{}'", getElement());
        waitForReadyState();
        return getElement().shouldBe(visible).getText();
    }

    public Button shouldContainText(String expectedPart) {
        log.info("Checking that button '{}' contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible).shouldHave(text(expectedPart));
        return self();
    }

    public Button shouldHaveExactText(String expectedText) {
        log.info("Checking that button '{}' has exact text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible).shouldHave(exactText(expectedText));
        return self();
    }
}
