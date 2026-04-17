package org.matbylin.gui.pages.qaplayground.practiceform.components;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Button;
import org.matbylin.gui.core.elements.Text;

@Getter
public class SubmittedForm extends BaseComponent<SubmittedForm> {

    private final Text titleText = new Text($("h3"));
    private final Text messageText = new Text($("p"));
    private final Button fillAgainButton = new Button($("#resetFormBtn"));

    public SubmittedForm(SelenideElement root) {
        super(root);
    }

    @Override
    public SubmittedForm shouldBeVisible() {
        super.shouldBeVisible();
        titleText.shouldBeVisible();
        return this;
    }
}
