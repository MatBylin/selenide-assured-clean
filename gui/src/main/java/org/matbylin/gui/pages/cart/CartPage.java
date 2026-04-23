package org.matbylin.gui.pages.cart;

import io.qameta.allure.Step;
import lombok.Getter;
import org.matbylin.core.models.product.ProductModel;
import org.matbylin.gui.core.components.generic.Footer;
import org.matbylin.gui.core.components.generic.TopBar;
import org.matbylin.gui.core.elements.Text;
import org.matbylin.gui.pages.BasePage;
import org.matbylin.gui.pages.cart.components.CartItems;

import static com.codeborne.selenide.Selenide.$;

public class CartPage extends BasePage<CartPage> {
    private static final String PAGE_TITLE = "Your Cart";
    private static final String URL = "/cart.html";

    @Getter
    private final Text pageTitle = new Text($(".title"));
    private final TopBar topBar = new TopBar($(".header_container"));
    private final Footer footer = new Footer($(".footer"));
    private final CartItems cartItems = new CartItems($("#cart_contents_container"));

    @Override
    @Step("Validating CartPage loaded")
    public void validateLoaded() {
        shouldHaveTitle(PAGE_TITLE);
        topBar.shouldBeVisible();
        cartItems.shouldBeVisible();
        footer.shouldBeVisible();
    }

    @Step("Validating that product is shown: {0}")
    public CartPage shouldShowProduct(ProductModel productModel) {
        cartItems.shouldContain(productModel);
        return this;
    }

    @Step("Opening CartPage url: " + URL)
    public CartPage openPage() {
        return openPage(URL);
    }
}
