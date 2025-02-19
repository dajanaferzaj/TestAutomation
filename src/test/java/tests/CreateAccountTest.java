package tests;

import base.BaseTest;
import org.p.pages.CreateAccountPage;
import org.p.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CreateAccountTest extends BaseTest {
    private final String testEmail = "johndoe" + System.currentTimeMillis() + "@gmail.com";
    private final String testPassword = "TestPassword123!";

    @Test
    public void testCreateAccount() {
        driver.get("https://magento.softwaretestingboard.com/");

        System.out.println("🔹 Creating a new account with:");
        System.out.println("   - Email: " + testEmail);
        System.out.println("   - Password: " + testPassword);

        // Navigate to Create Account Page
        HomePage home = new HomePage(driver);
        home.clickCreateAccount();

        // Fill Account Form and Submit
        CreateAccountPage createAccount = new CreateAccountPage(driver);
        createAccount.fillForm("John", "Doe", testEmail, testPassword);
        createAccount.submit();

        // Verify Success Message
        boolean accountCreated = createAccount.isSuccessMessageDisplayed();
        System.out.println("🔹 Account Created? " + accountCreated);
        Assert.assertTrue(accountCreated, "Account creation failed!");

        // Store credentials in a file
        try {
            Properties prop = new Properties();
            FileOutputStream fos = new FileOutputStream("credentials.properties");
            prop.setProperty("testEmail", testEmail);
            prop.setProperty("testPassword", testPassword);
            prop.store(fos, "Test Credentials");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Credentials saved to file.");
    }
}
