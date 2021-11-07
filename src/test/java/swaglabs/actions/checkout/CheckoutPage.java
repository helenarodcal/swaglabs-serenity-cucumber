package swaglabs.actions.checkout;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.ui.PageElement;
import org.openqa.selenium.By;
import swaglabs.model.CheckoutItem;

import java.util.Collection;

public class CheckoutPage {
    private static final Target THE_CART_ITEMS = PageElement.locatedBy(".cart_item").describedAs("Cart items");
    private static final By ITEM_QUANITTY = By.cssSelector(".cart_quantity");
    private static final By ITEM_DESCRIPTION = By.cssSelector(".inventory_item_name");
    private static final By ITEM_PRICE = By.cssSelector(".inventory_item_price");

    public static final Question<String> ITEM_TOTAL = Text.of(".summary_subtotal_label").map(PriceLabel::extractPrice);
    public static final Question<String> TAX = Text.of(".summary_tax_label").map(PriceLabel::extractPrice);;
    public static final Question<String> TOTAL = Text.of(".summary_total_label").map(PriceLabel::extractPrice);;

    public static Question<Collection<CheckoutItem>> items() {
        return THE_CART_ITEMS.mapAll(CheckoutPage::checkoutItemDetails);
    }

    private static CheckoutItem checkoutItemDetails(WebElementFacade cartItem) {
        int quantity = Integer.parseInt(cartItem.findBy(ITEM_QUANITTY).getText());
        String description = cartItem.findBy(ITEM_DESCRIPTION).getText();
        String price = cartItem.findBy(ITEM_PRICE).getText();
        return new CheckoutItem(quantity, description, price);
    }
}