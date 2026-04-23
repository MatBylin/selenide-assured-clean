package org.matbylin.gui.core.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;

@Slf4j
public class TableRow extends BaseElement<TableRow> {

    public TableRow(SelenideElement element) {
        super(element);
    }

    private ElementsCollection getCells() {
        shouldBeVisible();
        return getElement().$$(":scope td, :scope th");
    }

    public TableCell getCell(int columnIndex) {
        log.info("Returning cell with column index {} from row: '{}'", columnIndex, getElement());
        getCells().shouldHave(sizeGreaterThanOrEqual(columnIndex + 1));
        return new TableCell(getCells().get(columnIndex).shouldBe(visible));
    }

    public String getCellText(int columnIndex) {
        log.info("Returning cell text with column index {} from row: '{}'", columnIndex, getElement());
        return getCell(columnIndex).getText();
    }

    public String getText() {
        log.info("Returning text from element '{}'", getElement());
        waitForReadyState();
        getElement().shouldBe(visible);
        return getElement().getText();
    }

    public TableRow shouldContainText(String expectedPart) {
        log.info("Checking that element '{}' contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible);
        getElement().shouldHave(text(expectedPart));
        return self();
    }

    public TableRow shouldHaveExactText(String expectedText) {
        log.info("Checking that element '{}' has exact text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible);
        getElement().shouldHave(exactText(expectedText));
        return self();
    }
}
