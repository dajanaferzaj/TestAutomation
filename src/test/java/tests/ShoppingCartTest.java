package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.p.pages.FilterPage;
import org.p.pages.LoginPage;
import org.p.pages.ShoppingCartPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class ShoppingCartTest extends BaseTest {

    @Test
    public void testShoppingCart() {
        driver.get("https://magento.softwaretestingboard.com/");


        LoginPage login = new LoginPage(driver);
        login.openSignInPage();
        login.enterCredentials("johndoe1739984877158@gmail.com", "TestPassword123!");
        login.submitLogin();
        Assert.assertTrue(login.isUserLoggedIn(), "Login failed!");


        navigateToJackets();


        FilterPage filterPage = new FilterPage(driver);
        filterPage.applyColorFilter("Red");
        Assert.assertTrue(filterPage.areProductsDisplayedWithSelectedColor(), "Color filter did not apply correctly!");

        String priceRange = "$50.00 - $59.99";
        Assert.assertTrue(filterPage.applyPriceFilter(priceRange), "Price filter failed!");
        Assert.assertTrue(filterPage.areProductsWithinPriceRange(50.00, 59.99), "Price filter did not apply correctly!");


        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
        cartPage.addAllProductsToCart();


        Assert.assertTrue(cartPage.isSuccessMessageDisplayed(), "Success message not displayed!");


        cartPage.openShoppingCartFromSuccessMessage();


        Assert.assertTrue(cartPage.isOnShoppingCartPage(), "Did not navigate to the Shopping Cart page!");


        double subtotalFromPage = cartPage.getSubtotalFromSummary();
        double calculatedSubtotal = cartPage.totalProductPrice();
        double orderTotal = cartPage.getOrderSummaryTotal();

        System.out.println("Subtotal from Summary: $" + subtotalFromPage);
        System.out.println("Calculated Subtotal: $" + calculatedSubtotal);
        System.out.println("Order Total: $" + orderTotal);

        Assert.assertEquals(calculatedSubtotal, subtotalFromPage, "Subtotal mismatch!");
        Assert.assertEquals(subtotalFromPage, orderTotal, "Order total does not match subtotal!");
    }

    private void navigateToJackets() {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Women")))).perform();
        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Tops")))).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Jackets"))).click();
    }
}