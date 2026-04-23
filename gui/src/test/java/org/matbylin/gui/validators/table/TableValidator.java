package org.matbylin.gui.validators.table;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Step;
import org.matbylin.gui.core.elements.Table;

public class TableValidator {

    private final Table table;

    private TableValidator(Table table) {
        this.table = table;
    }

    public static TableValidator assertThat(Table table) {
        return new TableValidator(table);
    }

    @Step("Validating that table has non empty rows")
    public TableValidator hasNonEmptyRows() {
        table.getRows().shouldBe(CollectionCondition.sizeGreaterThan(0));
        return this;
    }

    @Step("Validating that table has exactly: {0} rows")
    public TableValidator hasExactNumberOfRows(int expectedNumberOfRows) {
        table.getRows().shouldBe(CollectionCondition.size(expectedNumberOfRows));
        return this;
    }

    @Step("Validating that table contains: {0} column headers")
    public TableValidator containsColumnHeaders(String... expectedHeaders) {
        table.getTableHeaders().shouldHave(CollectionCondition.containExactTextsCaseSensitive(expectedHeaders));
        return this;
    }

    @Step("Validating that table has exact: {0} column headers")
    public TableValidator hasExactColumnHeaders(String... expectedHeaders) {
        table.getTableHeaders().shouldHave(CollectionCondition.exactTexts(expectedHeaders));
        return this;
    }
}
