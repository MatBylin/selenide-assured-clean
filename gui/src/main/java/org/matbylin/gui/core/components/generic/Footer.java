package org.matbylin.gui.core.components.generic;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Button;

@Getter
public class Footer extends BaseComponent<Footer> {

    private final Button playgroundLogo = new Button($("span.gradient-subTitle"));

    public Footer(SelenideElement root) {
        super(root);
    }

    @Override
    public Footer shouldBeVisible() {
        playgroundLogo.shouldBeVisible();
        return this;
    }
}
