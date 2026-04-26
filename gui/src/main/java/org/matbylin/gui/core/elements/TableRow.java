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

    public TableCell getCell(int columnIndex) {
        log.info("Returning cell with column index {} from table row: '{}'", columnIndex, getElement());
        var allCells = getCells();
        allCells.shouldHave(sizeGreaterThanOrEqual(columnIndex + 1));
        return new TableCell(allCells.get(columnIndex).shouldBe(visible));
    }

    public String getCellText(int columnIndex) {
        log.info("Returning cell text with column index {} from table row: '{}'", columnIndex, getElement());
        return getCell(columnIndex).getText();
    }

    public String getText() {
        log.info("Returning text from table row '{}'", getElement());
        waitForReadyState();
        return getElement().shouldBe(visible).getText();
    }

    public TableRow shouldContainText(String expectedPart) {
        log.info("Checking that table row '{}' contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible).shouldHave(text(expectedPart));
        return self();
    }

    public TableRow shouldHaveExactText(String expectedText) {
        log.info("Checking that table row '{}' has exact text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible).shouldHave(exactText(expectedText));
        return self();
    }

    private ElementsCollection getCells() {
        waitForReadyState();
        return getElement().shouldBe(visible).$$(":scope td, :scope th");
    }
}
