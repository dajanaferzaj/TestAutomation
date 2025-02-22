package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.p.pages.LoginPage;
import org.p.pages.ShoppingCartPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class EmptyShoppingCartTest extends BaseTest {

    @Test
    public void testEmptyShoppingCart() {
        driver.get("https://magento.softwaretestingboard.com/");

        LoginPage login = new LoginPage(driver);
        login.openSignInPage();
        login.enterCredentials("johndoe1739984877158@gmail.com", "TestPassword123!");
        login.submitLogin();
        Assert.assertTrue(login.isUserLoggedIn(), "Login failed!");


        driver.get("https://magento.softwaretestingboard.com/checkout/cart/");
        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart.item"));
        int initialCount = cartItems.size();

        System.out.println("🛒 Initial items in cart: " + initialCount);
        Assert.assertTrue(initialCount > 0, "Cart is already empty before the test!");


        for (int i = initialCount; i > 0; i--) {
            cartItems = driver.findElements(By.cssSelector(".cart.item"));

            if (!cartItems.isEmpty()) {
                WebElement removeButton = cartItems.get(0).findElement(By.cssSelector(".action.action-delete"));
                removeButton.click();


                wait.until(ExpectedConditions.stalenessOf(cartItems.get(0)));

                int newCount = driver.findElements(By.cssSelector(".cart.item")).size();
                System.out.println("🛒 Items left in cart: " + newCount);
                Assert.assertEquals(newCount, i - 1, "Item count did not decrease correctly!");
            }
        }


        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-empty")));
        Assert.assertTrue(emptyCartMessage.isDisplayed(), "Empty cart message not displayed!");
        String actualMessage = emptyCartMessage.getText().trim().replaceAll("\\s+", " ");
        String expectedMessage = "You have no items in your shopping cart.";
        Assert.assertTrue(actualMessage.contains("You have no items"), "Incorrect empty cart message! Found: " + actualMessage);


        System.out.println("Shopping cart is empty!");


        driver.quit();
    }
}