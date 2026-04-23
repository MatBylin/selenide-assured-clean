package org.matbylin.gui.tests.datatable;

import io.qameta.allure.Story;
import org.matbylin.gui.groups.Tag;
import org.matbylin.gui.tests.BaseGuiTest;
import org.matbylin.gui.validators.table.TableValidator;
import org.testng.annotations.Test;

@Story("Data Table")
public class DataTableTests extends BaseGuiTest {
    @Test(groups = {Tag.SMOKE})
    void validateAutomationFormFilledSuccessfully() {
        var dataTablePage = dashboardPage
                .goToDataTablePage();

        TableValidator.assertThat(dataTablePage.getBooksTable())
                .hasNonEmptyRows()
                .hasExactNumberOfRows(10)
                .containsColumnHeaders("Book Name", "Book Genre", "Book Author");
    }
}
