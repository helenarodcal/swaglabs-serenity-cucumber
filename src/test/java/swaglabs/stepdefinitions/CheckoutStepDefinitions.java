package swaglabs.stepdefinitions;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.ensure.SoftlyEnsure;
import swaglabs.actions.cart.AShoppingCart;
import swaglabs.actions.cart.AddToCart;
import swaglabs.actions.checkout.*;
import swaglabs.actions.navigation.Navigate;
import swaglabs.model.CheckoutItem;
import swaglabs.model.CustomerDetails;
import swaglabs.model.TotalItemPrice;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutStepDefinitions {

    /**
     * The present tense tells us this is something Colin is doing now
     */
    @When("{actor} checks out his cart")
    public void colinInitiatesCheckout(Actor actor) {
        actor.attemptsTo(
                Checkout.theCurrentItemsInTheCart()
        );
    }

    /**
     * The past tense tells us we are setting up state, so Colin has added some items to his cart and has checkted them out
     */
    @Given("{actor} has selected an item and checked out his cart")
    public void colinHasCheckedOutHisCart(Actor actor) {
        actor.attemptsTo(
                AddToCart.item("Sauce Labs Onesie"),
                Checkout.theCurrentItemsInTheCart()
        );
    }

    @When("{actor} checks out his cart providing his personal details")
    public void checksOutWithPersonalDetails(Actor actor) {
        actor.attemptsTo(
                Navigate.toTheCheckoutPage(),
                ProvidePersonalDetails.of(CustomerDetails.about(actor.getName()))
        );

    }

    @Given("{actor} has the following items in his cart:")
    public void addItemsToCart(Actor actor, List<CheckoutItem> items) {
        List<String> itemNames = items.stream().map(CheckoutItem::description).collect(Collectors.toList());
        actor.has(AShoppingCart.containing(itemNames));
    }

    @When("{actor} checks out the following items:")
    public void checksOutItems(Actor actor, List<CheckoutItem> items) {
        List<String> itemNames = items.stream().map(CheckoutItem::description).collect(Collectors.toList());
        actor.attemptsTo(
                AddToCart.items(itemNames),
                Checkout.theCurrentItemsInTheCart(),
                ProvidePersonalDetails.of(CustomerDetails.about(actor.getName()))
        );
    }

    @When("{actor} confirms his order")
    public void confirmsOrder(Actor actor) {
        actor.attemptsTo(
                Confirm.order()
        );
    }

    @Then("{actor} should be informed {string}")
    public void shouldBeInformedThat(Actor actor, String message) {
        actor.attemptsTo(
                Ensure.that(ConfirmationPage.COMPLETE_MESSAGE).text().contains(message)
        );
    }

    @DataTableType
    public CheckoutItem product(Map<String, String> itemDetails) {
        return new CheckoutItem(
                Integer.parseInt(itemDetails.get("Qty")),
                itemDetails.get("Description"),
                itemDetails.get("Price"));
    }

    @DataTableType
    public TotalItemPrice itemPriceTotal(Map<String, String> itemTotals) {
        return new TotalItemPrice(
                itemTotals.get("Item total"),
                itemTotals.get("Tax"),
                itemTotals.get("Total"));
    }

    @Then("{actor} should be presented with a summary of his purchase including:")
    public void presentSummaryOfPurchases(Actor actor, List<CheckoutItem> expectedItems) {
        Collection<CheckoutItem> displayedItems = actor.asksFor(CheckoutPage.items());
        assertThat(displayedItems).containsOnlyElementsOf(expectedItems);
    }

    @Then("the total price should be:")
    public void totalPriceShouldBe(TotalItemPrice expectedPrices) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
                SoftlyEnsure.start(),
                Ensure.that(CheckoutPage.ITEM_TOTAL).isEqualTo(expectedPrices.itemTotal()),
                Ensure.that(CheckoutPage.TAX).isEqualTo(expectedPrices.tax()),
                Ensure.that(CheckoutPage.TOTAL).isEqualTo(expectedPrices.total()),
                SoftlyEnsure.finish()
        );
    }
}
