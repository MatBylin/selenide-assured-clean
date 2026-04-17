package org.matbylin.gui.pages.products;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;
import org.matbylin.gui.pages.products.components.Product;
import org.matbylin.gui.pages.products.components.Products;

import static com.codeborne.selenide.Selenide.$;

public class ProductsPage extends BasePage<ProductsPage> {
    private static final String PAGE_TITLE = "Products";

    @Getter
    private final Text pageTitle = new Text($(".title"));
    private final TopBar topBar = new TopBar($(".header_container"));
    private final Products products = new Products($("#inventory_container"));
    private final Footer footer = new Footer($(".footer"));

    @Override
    @Step("Validating ProductsPage loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
        topBar.shouldBeVisible();
        products.shouldBeVisible();
        footer.shouldBeVisible();
    }

    @Step("Getting product: {0}")
    public Product getProduct(String productName) {
        return products.getProduct(productName);
    }

    @Step("Validating product is visible: {0}")
    public ProductsPage shouldShowProduct(ProductModel productModel) {
        products.shouldContain(productModel);
        return this;
    }

    @Step("Adding product to cart: {0}")
    public ProductsPage addProductToCart(ProductModel productModel) {
        products.addProductToCart(productModel.getName());
        return this;
    }
}
