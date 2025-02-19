package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.p.pages.FilterPage;
import org.p.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class FilterTest extends BaseTest {

    @Test
    public void testPageFilters() {
        driver.get("https://magento.softwaretestingboard.com/");

        LoginPage login = new LoginPage(driver);
        login.openSignInPage();
        login.enterCredentials("johndoe1739984877158@gmail.com", "TestPassword123!");
        login.submitLogin();
        Assert.assertTrue(login.isUserLoggedIn(), "❌ Login failed!");

        navigateToJackets();

        FilterPage filterPage = new FilterPage(driver);

        filterPage.applyColorFilter("Black");
        Assert.assertTrue(filterPage.areProductsDisplayedWithSelectedColor(), "❌ Color filter did not apply correctly!");

        filterPage.applyPriceFilter("50-59");
        Assert.assertTrue(filterPage.areProductsWithinPriceRange(50.00, 59.99), "❌ Price filter did not apply correctly!");
    }

    private void navigateToJackets() {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Women")))).perform();
        actions.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Tops")))).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Jackets"))).click();
    }
}
