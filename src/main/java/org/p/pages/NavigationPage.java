// pages/NavigationPage.java
package org.p.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavigationPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public NavigationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    public void navigateToJackets() {
        WebElement womenMenu = driver.findElement(By.linkText("Women"));
        actions.moveToElement(womenMenu).perform();

        WebElement topsMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Tops")));
        actions.moveToElement(topsMenu).perform();

        WebElement jacketsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Jackets")));
        jacketsMenu.click();
    }

    // Add other navigation methods as needed...
    public void navigateToHomePage() {
        driver.get("https://magento.softwaretestingboard.com/");
    }

    public void navigateToSignInPage(){
        driver.findElement(By.linkText("Sign In")).click();
    }

    public void navigateToGearBags(){
        WebElement gearMenu = driver.findElement(By.linkText("Gear"));
        actions.moveToElement(gearMenu).perform();

        WebElement bagsMenu = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Bags")));
        bagsMenu.click();
    }
}