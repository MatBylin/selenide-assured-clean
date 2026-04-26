package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.Condition.visible;

@Slf4j
public class TableCell extends BaseElement<TableCell> {

    public TableCell(SelenideElement element) {
        super(element);
    }

    public String getText() {
        log.info("Returning text from table cell '{}'", getElement());
        waitForReadyState();
        return getElement().shouldBe(visible).getText();
    }
}
