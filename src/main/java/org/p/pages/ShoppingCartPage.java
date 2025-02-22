package org.p.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ShoppingCartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }


    public void addAllProductsToCart() {
        List<WebElement> addToCartButtons = driver.findElements(By.cssSelector(".product-item .tocart"));

        for (WebElement button : addToCartButtons) {
            try {
                WebElement parentElement = button.findElement(By.xpath("./ancestor::div[contains(@class, 'product-item')]"));
                WebElement sizeOption = parentElement.findElement(By.cssSelector(".swatch-option"));


                if (sizeOption.isDisplayed()) {
                    sizeOption.click();
                }

                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
            } catch (Exception e) {
                System.out.println("Error clicking 'Add to Cart' button: " + e.getMessage());
            }
        }
    }


    public boolean isSuccessMessageDisplayed() {
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
            return successMessage.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Success message did not appear.");
            return false;
        }
    }


    public void openShoppingCartFromSuccessMessage() {
        try {
            WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".message-success a[href*='checkout/cart']")));
            cartLink.click();
        } catch (Exception e) {
            System.out.println("Error opening cart from success message: " + e.getMessage());
        }
    }

    public boolean isOnShoppingCartPage() {
        try {
            WebElement cartTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.page-title")));
            return cartTitle.getText().contains("Shopping Cart");
        } catch (Exception e) {
            return false;
        }
    }

    public double getSubtotalFromSummary() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-totals .sub .price")));
            WebElement subtotalElement = driver.findElement(By.cssSelector(".cart-totals .sub .price"));
            String subtotalText = subtotalElement.getText().replaceAll("[^0-9.]", "").trim();
            return Double.parseDouble(subtotalText);
        } catch (Exception e) {
            System.out.println("Error retrieving subtotal: " + e.getMessage());
            return 0.0;
        }
    }


    public double getOrderSummaryTotal() {
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-totals .grand .price")));


            WebElement totalElement = driver.findElement(By.cssSelector(".cart-totals .grand .price"));
            String totalText = totalElement.getText();


            System.out.println("Raw total text: " + totalText);


            totalText = totalText.replaceAll("[^0-9.]", "").trim();


            System.out.println("Cleaned total text: " + totalText);

            if (!totalText.isEmpty()) {
                return Double.parseDouble(totalText);
            } else {
                System.out.println("Total text is empty after cleaning.");
                return 0.0;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving order total: " + e.getMessage());
            return 0.0;
        }
    }


    public double totalProductPrice() {
        try {
            return getOrderSummaryTotal();
        } catch (Exception e) {
            System.out.println("Error calculating total product price: " + e.getMessage());
            return 0.0;
        }
    }

}