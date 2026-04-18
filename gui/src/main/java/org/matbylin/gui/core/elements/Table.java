package org.matbylin.gui.core.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.matbylin.gui.core.elements.base.BaseElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;

@Slf4j
public class Table extends BaseElement<Table> {

    public Table(SelenideElement element) {
        super(element);
    }

    public String getText() {
        log.info("Returning text from element '{}'", getElement());
        getElement().shouldBe(visible);
        return getElement().getText();
    }

    public Table shouldContainText(String expectedPart) {
        log.info("Checking that element '{}' contains text '{}'", getElement(), expectedPart);
        getElement().shouldBe(visible);
        getElement().shouldHave(text(expectedPart));
        return self();
    }

    public Table shouldHaveExactText(String expectedText) {
        log.info("Checking that element '{}' has exact text '{}'", getElement(), expectedText);
        getElement().shouldBe(visible);
        getElement().shouldHave(exactText(expectedText));
        return self();
    }

    public int getRowCount() {
        log.info("Returning row count of table: '{}'", getElement());
        shouldExist();
        return rows().size();
    }

    public TableRow row(int rowIndexZeroBased) {
        log.info("Returning row with index {} from table: '{}'", rowIndexZeroBased, getElement());
        var allRows = rows();
        allRows.shouldHave(sizeGreaterThanOrEqual(rowIndexZeroBased + 1));
        return new TableRow(allRows.get(rowIndexZeroBased).shouldBe(visible));
    }

    public TableCell cell(int rowIndexZeroBased, int columnIndexZeroBased) {
        log.info("Returning cell from table: '{}', row index: {}, column index: {}", getElement(), rowIndexZeroBased, columnIndexZeroBased);
        return row(rowIndexZeroBased).cell(columnIndexZeroBased);
    }

    public String getCellText(int rowIndexZeroBased, int columnIndexZeroBased) {
        log.info("Returning cell text from table: '{}', row index: {}, column index: {}", getElement(), rowIndexZeroBased, columnIndexZeroBased);
        return cell(rowIndexZeroBased, columnIndexZeroBased).getText();
    }

    public TableRow rowContaining(String expectedText) {
        log.info("Returning row from table: '{}', containing text '{}'", getElement(), expectedText);
        return new TableRow(rows().findBy(text(expectedText)).shouldBe(visible));
    }

    private ElementsCollection rows() {
        shouldBeVisible();
        ElementsCollection bodyRows = getElement().$$x("./tbody/tr");
        if (!bodyRows.isEmpty()) {
            return bodyRows;
        }
        return getElement().$$x("./tr[td]");
    }
}
