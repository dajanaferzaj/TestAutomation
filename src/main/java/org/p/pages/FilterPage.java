package org.p.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FilterPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public FilterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased timeout
        this.actions = new Actions(driver);
    }

    public void removeActiveFilters() {
        try {
            List<WebElement> activeFilters = driver.findElements(By.cssSelector(".filter-current .items li a"));

            if (!activeFilters.isEmpty()) {
                for (WebElement filter : activeFilters) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filter);
                    filter.click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
                }
                System.out.println("✅ All active filters removed.");
            } else {
                System.out.println("ℹ️ No active filters to remove.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error removing filters: " + e.getMessage());
        }
    }

    public void applyColorFilter(String color) {
        removeActiveFilters();  // 🔹 Ensure filters are cleared before applying a new one

        try {
            WebElement colorFilterSection = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='narrow-by-list']//div[text()='Color']")));

            if (!colorFilterSection.getAttribute("class").contains("active")) {
                colorFilterSection.click();
            }

            WebElement colorOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='narrow-by-list']//div[@class='swatch-attribute-options clearfix']//div[@option-label='" + color + "']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colorOption);
            colorOption.click();

            wait.until(ExpectedConditions.urlContains("color"));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));

            System.out.println("✅ Color filter applied: " + color);
        } catch (Exception e) {
            System.out.println("❌ Error applying color filter: " + e.getMessage());
        }
    }

    public void applyPriceFilter(String priceRange) {
        removeActiveFilters();  // 🔹 Ensure filters are cleared before applying a new one

        try {
            // Step 1: Locate the Price filter section
            WebElement priceFilterSection = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='narrow-by-list']//div[text()='Price']")));
            System.out.println("✅ Price filter section located.");

            // Step 2: Expand the Price filter dropdown (if not already expanded)
            if (!priceFilterSection.getAttribute("class").contains("active")) {
                priceFilterSection.click();
                System.out.println("✅ Price filter dropdown expanded.");
            }

            // Debugging: Print the HTML of the price filter section
            WebElement priceFilterContent = driver.findElement(By.xpath("//div[@id='narrow-by-list']//div[text()='Price']/following-sibling::div"));
            System.out.println("Price Filter Content: " + priceFilterContent.getAttribute("innerHTML"));

            // Step 3: Locate the specific price range option
            WebElement priceOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='narrow-by-list']//a[contains(text(), '" + priceRange + "')]")));
            System.out.println("✅ Price range option located: $" + priceRange);

            // Step 4: Scroll into view and click the price range option
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceOption);
            priceOption.click();
            System.out.println("✅ Price range option selected: $" + priceRange);

            // Step 5: Wait for the products to update
            wait.until(ExpectedConditions.urlContains("price=" + priceRange));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
            System.out.println("✅ Products updated after applying price filter.");
        } catch (Exception e) {
            System.out.println("❌ Error applying price filter: " + e.getMessage());
        }
    }

    public boolean areProductsDisplayedWithSelectedColor() {
        List<WebElement> productItems = driver.findElements(By.cssSelector(".product-item"));

        if (productItems.isEmpty()) {
            System.out.println("❌ No products displayed after filtering.");
            return false;
        }

        for (WebElement productItem : productItems) {
            List<WebElement> swatches = productItem.findElements(By.cssSelector(".swatch-option.color"));

            if (swatches.isEmpty()) {
                System.out.println("❌ No color swatches found in a product.");
                return false;
            }

            for (WebElement swatch : swatches) {
                if (swatch.getAttribute("class").contains("selected")) {
                    return true;
                }
            }
        }

        System.out.println("❌ No products found with the selected color.");
        return false;
    }

    public boolean areProductsWithinPriceRange(double minPrice, double maxPrice) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".product-item")));

            List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));

            if (products.isEmpty()) {
                System.out.println("❌ No products found after applying the price filter.");
                return false;
            }

            for (WebElement product : products) {
                List<WebElement> priceElements = product.findElements(By.cssSelector(".price"));

                if (priceElements.isEmpty()) {
                    System.out.println("❌ Product has no price displayed.");
                    return false;
                }

                // Get the actual price (handle discount/original prices)
                String priceText = priceElements.get(priceElements.size() - 1).getText().replaceAll("[^0-9.]", "").trim();
                double price = Double.parseDouble(priceText);

                System.out.println("Product Price: $" + price); // Debugging: Print the price of each product

                if (price < minPrice || price > maxPrice) {
                    System.out.println("❌ Product out of range: $" + price);
                    return false;
                }
            }

            System.out.println("✅ All products match the price range.");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying price filter: " + e.getMessage());
            return false;
        }
    }
}