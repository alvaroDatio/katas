package com.katas;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DiscountStepdefs {

    private static final List<Book> series = Arrays.asList(
            new Book("Harry Potter I", 8D),
            new Book("Harry Potter II", 8D),
            new Book("Harry Potter III", 8D),
            new Book("Harry Potter IV", 8D),
            new Book("Harry Potter V", 8D)
    );

    private ShoppingCart cart = new ShoppingCart();

    @When("^I buy (\\d+) cop(?:y|ies) of \"([^\"]*)\"$")
    public void I_buy_copy_of(int numCopies, String title) throws Throwable {
        Book book = bookByTitle(title).orElseThrow(() -> new UnknownTitle(title));

        if (numCopies>0)
            cart.addBook(book,numCopies);
    }


    @Then("^I must pay \\$(\\d+.?\\d*)$")
    public void I_must_pay_$(Double amount) throws Throwable {
        Double expected = amount;
        Double total = cart.calculateTotalWithDiscounts();
        Assert.assertEquals(total, expected);
    }

    private Optional<Book> bookByTitle(String title) {
        return series.stream()
                .filter(book -> title.equals(book.getTitle()))
                .findFirst();
    }

    private static class UnknownTitle extends RuntimeException {
        public UnknownTitle(String title) {
            super(title);
        }
    }
}
