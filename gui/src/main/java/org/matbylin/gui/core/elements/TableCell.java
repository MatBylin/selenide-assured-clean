package org.matbylin.gui.core.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableCell extends Text {

    public TableCell(SelenideElement element) {
        super(element);
    }
}
