package org.matbylin.gui.pages.qaplayground.dashboard;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;
import org.matbylin.gui.pages.qaplayground.dashboard.components.Tile;
import org.matbylin.gui.pages.qaplayground.datatable.DataTablePage;
import org.matbylin.gui.pages.qaplayground.forms.PracticeFormPage;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage extends BasePage<DashboardPage> {
    private static final String PAGE_TITLE = "Ready to be a Pro AI Automation Engineer?";

    @Getter
    private final Text pageTitle = new Text($("[class='page-title py-4'] h1"));
    private final TopBar topBar = new TopBar($("header"));
    private final Footer footer = new Footer($("footer"));
    private final Tile formsAutomationTile = new Tile($("#card-link-forms"));
    private final Tile tableAutomationTile = new Tile($("#card-link-data-table"));

    @Override
    @Step("Validating Dashboard page loaded")
    public void validateLoaded() {
        pageTitle.shouldBeVisible();
        topBar.shouldBeVisible();
        footer.shouldBeVisible();
        shouldHaveTitle(PAGE_TITLE);
    }

    @Step("Going to Practice Form page")
    public PracticeFormPage goToPracticeFormPage() {
        formsAutomationTile.getGoToButton().click();
        return new PracticeFormPage().shouldBeLoaded();
    }

    @Step("Going to Data Table page")
    public DataTablePage goToDataTablePage() {
        tableAutomationTile.getGoToButton().click();
        return new DataTablePage().shouldBeLoaded();
    }
}
