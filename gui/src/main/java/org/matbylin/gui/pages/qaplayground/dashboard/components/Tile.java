package org.matbylin.gui.pages.qaplayground.dashboard.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Button;
import org.matbylin.gui.core.elements.Text;

@Getter
public class Tile extends BaseComponent<Tile> {

    private final Text titleText = new Text($("p.px-2"));
    private final Text contentText = new Text($("p.py-7"));
    private final Button goToButton = new Button($("span.underline"));

    public Tile(SelenideElement root) {
        super(root);
    }

    @Override
    public Tile shouldBeVisible() {
        super.shouldBeVisible();
        goToButton.shouldBeVisible();
        return this;
    }

    @Step("Going to section from Dashboard page")
    public void goToSection() {
        goToButton.click();
    }
}
