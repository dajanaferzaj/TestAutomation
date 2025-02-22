package org.p.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WishlistPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public WishlistPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void removePriceFilter() {
        try {
            List<WebElement> appliedFilters = driver.findElements(By.cssSelector(".filter-current .item"));

            for (WebElement filter : appliedFilters) {
                if (filter.getText().contains("Price")) {
                    WebElement removeButton = filter.findElement(By.cssSelector("a.action.remove"));
                    removeButton.click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
                    System.out.println("✅ Price filter removed successfully.");
                    return;
                }
            }

            System.out.println("⚠ Price filter not found (may already be removed).");

        } catch (Exception e) {
            System.out.println("Failed to remove price filter: " + e.getMessage());
        }
    }


    public int getDisplayedItemsCount() {
        return driver.findElements(By.cssSelector(".product-item")).size();
    }


    public void addFirstTwoItemsToWishlist() {
        try {

            WebElement firstItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='maincontent']/div[3]/div[1]/div[3]/ol/li[1]/div/div/div[4]/div/div[2]/a[1]")));
            firstItem.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
            System.out.println("✅ First item added to Wishlist.");


            driver.navigate().back();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".products-grid")));
            System.out.println("🔄 Returned to product listing page.");


            WebElement secondItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='maincontent']/div[3]/div[1]/div[3]/ol/li[2]/div/div/div[4]/div/div[2]/a[1]")));
            secondItem.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
            System.out.println("Second item added to Wishlist.");

        } catch (Exception e) {
            System.out.println("Failed to add items to Wishlist: " + e.getMessage());
        }
    }


    public boolean isWishlistSuccessDisplayed() {
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success")));
            WebElement successIcon = driver.findElement(By.cssSelector(".message-success .icon"));
            System.out.println("Wishlist success message displayed: " + successMessage.getText());
            return successMessage.isDisplayed() && successIcon.isDisplayed();
        } catch (Exception e) {
            System.out.println("Success message not displayed: " + e.getMessage());
            return false;
        }
    }


    public boolean isWishlistCountCorrect(int expectedCount) {
        try {
            WebElement userProfile = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".customer-name")));
            userProfile.click();
            WebElement wishlistCount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'My Wish List')]")));

            String text = wishlistCount.getText();
            boolean isCorrect = text.contains("My Wish List (" + expectedCount + " items)");
            System.out.println("✅ Wishlist count displayed correctly: " + text);
            return isCorrect;
        } catch (Exception e) {
            System.out.println("Wishlist count incorrect: " + e.getMessage());
            return false;
        }
    }
}
