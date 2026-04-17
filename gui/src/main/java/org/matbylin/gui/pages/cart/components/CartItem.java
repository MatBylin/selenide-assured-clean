package org.matbylin.gui.pages.cart.components;

import com.codeborne.selenide.SelenideElement;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Text;

public class CartItem extends BaseComponent<CartItem> {

    private final Text name = new Text($(".inventory_item_name"));
    private final Text description = new Text($(".inventory_item_desc"));
    private final Text price = new Text($(".inventory_item_price"));

    public CartItem(SelenideElement root) {
        super(root);
    }

    @Override
    public CartItem shouldBeVisible() {
        name.shouldBeVisible();
        description.shouldBeVisible();
        price.shouldBeVisible();
        return this;
    }

    public CartItem shouldMatch(ProductModel productModel) {
        name.shouldHaveExactText(productModel.getName());
        description.shouldHaveExactText(productModel.getDescription());
        price.shouldHaveExactText(productModel.getPrice());
        return this;
    }
}
