package org.p.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;

    // Locators
    private By signInLink = By.linkText("Sign In");
    private By emailField = By.id("email");
    private By passwordField = By.id("pass");
    private By loginButton = By.id("send2");
    private By welcomeMessage = By.cssSelector("div.panel.header li.greet");  // ✅ Updated Locator
    private By signOutLink = By.linkText("Sign Out");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openSignInPage() {
        driver.findElement(signInLink).click();
    }

    public void enterCredentials(String email, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
    }

    public void submitLogin() {
        driver.findElement(loginButton).click();

        // Wait for "Welcome, John Doe!" text
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(welcomeMessage));
    }

    public boolean isUserLoggedIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            System.out.println("🔹 Checking if user is logged in...");
            WebElement welcomeText = wait.until(ExpectedConditions.presenceOfElementLocated(welcomeMessage));
            boolean isUserVisible = welcomeText.isDisplayed();

            System.out.println("✅ Login Success: " + isUserVisible);
            return isUserVisible;
        } catch (Exception e) {
            System.out.println(" Login Failed - Exception: " + e.getMessage());
            return false;
        }
    }

    public void signOut() {
        driver.findElement(signOutLink).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(signInLink));
    }
}
