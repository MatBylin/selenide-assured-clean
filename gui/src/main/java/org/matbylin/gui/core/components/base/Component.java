package org.matbylin.gui.core.components.base;

import com.codeborne.selenide.SelenideElement;

public interface Component<T> {
    SelenideElement getRoot();

    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }
}
