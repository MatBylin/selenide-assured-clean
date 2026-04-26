package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Text extends BaseElement<Text> {

    public Text(SelenideElement element) {
        super(element);
    }

    public String getText() {
        log.info("Returning text from '{}'", getElement());
        waitForReadyState();
        return getElement().shouldBe(visible).getText();
    }

    public Text shouldContainText(String expectedPart) {
        log.info("Checking that '{}' contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible).shouldHave(text(expectedPart));
        return self();
    }

    public Text shouldNotContainText(String expectedPart) {
        log.info("Checking that '{}' not contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible).shouldNotHave(text(expectedPart));
        return self();
    }

    public Text shouldHaveExactText(String expectedText) {
        log.info("Checking that '{}' has exact text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible).shouldHave(exactText(expectedText));
        return self();
    }
}
