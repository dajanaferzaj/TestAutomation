package org.p.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EmptyShoppingCartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public EmptyShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    public void emptyCart() {
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart.item"));

        while (!cartItems.isEmpty()) {
            WebElement removeButton = cartItems.get(0).findElement(By.cssSelector(".action.action-delete"));
            removeButton.click();
            wait.until(ExpectedConditions.stalenessOf(cartItems.get(0)));
            cartItems = driver.findElements(By.cssSelector(".cart.item"));
        }
    }


    public boolean isCartEmpty() {
        try {
            WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-empty")));
            return emptyCartMessage.getText().trim().equals("You have no items in your shopping cart.");
        } catch (Exception e) {
            return false;
        }
    }
}
