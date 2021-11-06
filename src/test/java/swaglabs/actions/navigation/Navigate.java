package swaglabs.actions.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Open;

public class Navigate {
    public static Performable toTheShoppingCart() {
        return Open.url("https://www.saucedemo.com/cart.html");
    }
    public static Performable toTheCheckoutPage() {
        return Open.url("https://www.saucedemo.com/checkout-step-one.html");
    }
}