package org.matbylin.gui.pages.cart.components;

import com.codeborne.selenide.SelenideElement;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.base.BaseComponent;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class CartItems extends BaseComponent<CartItems> {

    public CartItems(SelenideElement root) {
        super(root);
    }

    @Override
    public CartItems shouldBeVisible() {
        super.shouldBeVisible();
        getRoot().$(".cart_list").shouldBe(visible);
        return this;
    }

    public List<CartItem> getAll() {
        shouldBeVisible();
        return getRoot().$$(".cart_item").stream()
                .map(CartItem::new)
                .toList();
    }

    public CartItem getCartItem(String productName) {
        shouldBeVisible();
        var productNameElement = getRoot().$$(".inventory_item_name")
                .findBy(exactText(productName))
                .shouldBe(visible);
        return new CartItem(productNameElement.ancestor(".cart_item").shouldBe(visible));
    }

    public CartItems shouldContain(ProductModel productModel) {
        getCartItem(productModel.getName()).shouldMatch(productModel);
        return this;
    }
}
