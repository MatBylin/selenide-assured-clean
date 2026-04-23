package org.matbylin.gui.pages.qaplayground.datatable;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Table;
import org.matbylin.gui.core.elements.TableRow;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Getter
public class DataTablePage extends BasePage<DataTablePage> {
    private static final String PAGE_TITLE = "Data Table Automation Practice";

    private final Text pageTitle = new Text($("section h1"));
    private final TopBar topBar = new TopBar($("header"));
    private final Footer footer = new Footer($("footer"));
    private final Table booksTable = new Table($("#books-table"));
    private final TableRow loadingRow = new TableRow($x("//td[contains(., 'Loading books...')]"));

    @Override
    @Step("Validating Data Table page loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
        loadingRow.shouldBe(not(exist));
    }
}
