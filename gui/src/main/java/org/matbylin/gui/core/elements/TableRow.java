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

    public TableCell cell(int columnIndexZeroBased) {
        log.info("Returning cell with column index {} from row: '{}'", columnIndexZeroBased, getElement());
        cells().shouldHave(sizeGreaterThanOrEqual(columnIndexZeroBased + 1));
        return new TableCell(cells().get(columnIndexZeroBased).shouldBe(visible));
    }

    public String getCellText(int columnIndexZeroBased) {
        log.info("Returning cell text with column index {} from row: '{}'", columnIndexZeroBased, getElement());
        return cell(columnIndexZeroBased).getText();
    }

    private ElementsCollection cells() {
        shouldBeVisible();
        return getElement().$$(":scope td, :scope th");
    }
}
