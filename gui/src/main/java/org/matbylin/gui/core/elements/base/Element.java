package org.matbylin.gui.core.elements.base;

import com.codeborne.selenide.SelenideElement;

public interface Element<T> {
    SelenideElement getElement();

    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }
}
