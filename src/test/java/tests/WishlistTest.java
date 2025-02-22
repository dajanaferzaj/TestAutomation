package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.p.pages.FilterPage;
import org.p.pages.LoginPage;
import org.p.pages.WishlistPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class WishlistTest extends BaseTest {

    @Test
    public void testWishlistFunctionality() {
        driver.get("https://magento.softwaretestingboard.com/");

        LoginPage login = new LoginPage(driver);
        login.openSignInPage();
        login.enterCredentials("johndoe1739984877158@gmail.com", "TestPassword123!");
        login.submitLogin();
        Assert.assertTrue(login.isUserLoggedIn(), "Login failed!");

        navigateToJackets();

        FilterPage filterPage = new FilterPage(driver);
        WishlistPage wishlistPage = new WishlistPage(driver);


        filterPage.applyColorFilter("Red");


        boolean priceApplied = filterPage.applyPriceFilter("$50.00 - $59.99");
        Assert.assertTrue(priceApplied, "Price filter did not apply correctly!");


        int itemsBefore = wishlistPage.getDisplayedItemsCount();
        System.out.println("Items before removing price filter: " + itemsBefore);


        wishlistPage.removePriceFilter();


        int itemsAfter = wishlistPage.getDisplayedItemsCount();
        System.out.println("Items after removing price filter: " + itemsAfter);
        Assert.assertTrue(itemsAfter > itemsBefore, "Items did not increase after removing price filter!");


        addFirstTwoItemsToWishlist(wishlistPage);


        Assert.assertTrue(isWishlistSuccessDisplayed(), "Success message not displayed!");


        verifyWishlistCountInProfile();
    }

    private void navigateToJackets() {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Women")))).perform();
        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Tops")))).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Jackets"))).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
        System.out.println("Navigated to Jackets category.");
    }

    private void addFirstTwoItemsToWishlist(WishlistPage wishlistPage) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));


            List<WebElement> wishlistButtons = driver.findElements(By.xpath("//*[@id='maincontent']/div[3]/div[1]/div[3]/ol/li/div/div/div[4]/div/div[2]/a[1]"));

            if (wishlistButtons.isEmpty()) {
                System.out.println("Wishlist buttons NOT found! Check the page structure.");
                Assert.fail("Wishlist buttons not found.");
                return;
            }

            System.out.println("Wishlist buttons found: " + wishlistButtons.size());


            WebElement firstItem = wishlistButtons.get(0);
            scrollAndClick(firstItem, wait);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'message-success')]")));
            System.out.println("First item added to Wishlist.");

            driver.navigate().back();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".products-grid")));
            System.out.println("Returned to product listing page. Re-fetching elements...");

            wishlistButtons = driver.findElements(By.xpath("//*[@id='maincontent']/div[3]/div[1]/div[3]/ol/li/div/div/div[4]/div/div[2]/a[1]"));

            if (wishlistButtons.size() < 2) {
                System.out.println("Second wishlist button not found after refresh!");
                Assert.fail("Second wishlist button missing.");
                return;
            }


            WebElement secondItem = wishlistButtons.get(1);
            scrollAndClick(secondItem, wait);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'message-success')]")));
            System.out.println("Second item added to Wishlist.");

        } catch (Exception e) {
            System.out.println("Failed to add items to Wishlist: " + e.getMessage());
            Assert.fail("Test failed due to Wishlist item addition issue.");
        }
    }

    private void scrollAndClick(WebElement element, WebDriverWait wait) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("Normal click failed, using JavaScript click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private boolean isWishlistSuccessDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));


            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'message-success')]")));

            String successText = successMessage.getText();
            System.out.println("Wishlist success message displayed: " + successText);

            return successText.contains("has been added to your Wish List");

        } catch (TimeoutException e) {
            System.out.println("Timeout: Success message not found in time.");
        } catch (Exception e) {
            System.out.println("Unexpected error while checking success message: " + e.getMessage());
        }
        return false;
    }

    private void verifyWishlistCountInProfile() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


            WebElement profileMenuButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.action.switch[data-action='customer-menu-toggle']")));
            profileMenuButton.click();
            System.out.println("Opened user profile menu.");


            WebElement wishlistCounter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.counter.qty[data-bind='text: wishlist().counter']")));


            String wishlistText = wishlistCounter.getText().trim();
            System.out.println("Wishlist count in profile: " + wishlistText);


            Assert.assertEquals(wishlistText, "2 items", "Wishlist count in profile is incorrect!");

        } catch (Exception e) {
            System.out.println("Error verifying wishlist count in profile: " + e.getMessage());
            Assert.fail("Test failed due to wishlist count validation issue.");
        }
    }
}
