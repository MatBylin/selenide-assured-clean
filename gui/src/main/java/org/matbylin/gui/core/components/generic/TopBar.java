package org.matbylin.gui.core.components.generic;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Button;

import static com.codeborne.selenide.Selectors.byText;

@Getter
public class TopBar extends BaseComponent<TopBar> {

    private final Button homeButton = new Button($(byText("Home")));
    private final Button studyTrackerButton = new Button($(byText("Study Tracker")));
    private final Button bankDemoButton = new Button($(byText("Bank Demo")));

    public TopBar(SelenideElement root) {
        super(root);
    }

    @Override
    public TopBar shouldBeVisible() {
        super.shouldBeVisible();
        homeButton.shouldBeVisible();
        studyTrackerButton.shouldBeVisible();
        bankDemoButton.shouldBeVisible();
        return this;
    }
}
