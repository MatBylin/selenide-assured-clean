package org.matbylin.gui.pages.products.components;

import com.codeborne.selenide.SelenideElement;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.base.BaseComponent;

import java.util.List;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class Products extends BaseComponent<Products> {

    public Products(SelenideElement root) {
        super(root);
    }

    @Override
    public Products shouldBeVisible() {
        super.shouldBeVisible();
        $$(".inventory_item").first().shouldBe(visible);
        return this;
    }

    public List<Product> getAll() {
        shouldBeVisible();
        return $$(".inventory_item").stream()
                .map(Product::new)
                .toList();
    }

    public Product getProduct(String productName) {
        shouldBeVisible();
        var productNameElement = $$(".inventory_item_name")
                .findBy(exactText(productName))
                .as("found product: " + productName)
                .shouldBe(visible);
        return new Product(productNameElement.ancestor(".inventory_item").shouldBe(visible));
    }

    public Products shouldContain(ProductModel productModel) {
        getProduct(productModel.getName()).shouldMatch(productModel);
        return this;
    }

    public Products addProductToCart(String productName) {
        getProduct(productName).addToCart();
        return this;
    }
}
