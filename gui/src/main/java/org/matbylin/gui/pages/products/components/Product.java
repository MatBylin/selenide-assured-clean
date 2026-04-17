package org.matbylin.gui.pages.products.components;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.base.BaseComponent;
import org.matbylin.gui.core.elements.Button;
import org.matbylin.gui.core.elements.Text;

@Getter
public class Product extends BaseComponent<Product> {

    private final Text name = new Text($(".inventory_item_name"));
    private final Text description = new Text($(".inventory_item_desc"));
    private final Text price = new Text($(".inventory_item_price"));
    private final Button actionButton = new Button($("button"));

    public Product(SelenideElement root) {
        super(root);
    }

    @Override
    public Product shouldBeVisible() {
        name.shouldBeVisible();
        description.shouldBeVisible();
        price.shouldBeVisible();
        actionButton.shouldBeVisible();
        return this;
    }

    public Product addToCart() {
        actionButton.click();
        return this;
    }

    public Product shouldMatch(ProductModel productModel) {
        name.shouldHaveExactText(productModel.getName());
        description.shouldHaveExactText(productModel.getDescription());
        price.shouldHaveExactText(productModel.getPrice());
        return this;
    }
}
