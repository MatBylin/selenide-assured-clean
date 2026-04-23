package org.matbylin.gui.core.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@Slf4j
public class Table extends BaseElement<Table> {

    public Table(SelenideElement element) {
        super(element);
    }

    public ElementsCollection getTableHeaders() {
        shouldBeVisible();
        var tableHeaders = getElement().$$x("./thead//th");
        if (!tableHeaders.isEmpty()) {
            return tableHeaders;
        }
        return getElement().$$x(".//th");
    }

    public ElementsCollection getRows() {
        shouldBeVisible();
        var bodyRows = getElement().$$x("./tbody/tr");
        if (!bodyRows.isEmpty()) {
            return bodyRows;
        }
        return getElement().$$x("./tr[td]");
    }

    public TableRow getRow(int rowIndex) {
        log.info("Returning row with index {} from table: '{}'", rowIndex, getElement());
        var allRows = getRows();
        allRows.shouldHave(sizeGreaterThanOrEqual(rowIndex + 1));
        return new TableRow(allRows.get(rowIndex).shouldBe(visible));
    }

    public TableCell getCell(int rowIndex, int columnIndex) {
        log.info("Returning cell from table: '{}', row index: {}, column index: {}", getElement(), rowIndex, columnIndex);
        return getRow(rowIndex).getCell(columnIndex);
    }

    public TableRow getRowContaining(String expectedText) {
        log.info("Returning row from table: '{}', containing text '{}'", getElement(), expectedText);
        return new TableRow(getRows().findBy(text(expectedText)).shouldBe(visible));
    }

    public String getCellText(int rowIndex, int columnIndex) {
        log.info("Returning cell text from table: '{}', row index: {}, column index: {}", getElement(), rowIndex, columnIndex);
        return getCell(rowIndex, columnIndex).getText();
    }
}
