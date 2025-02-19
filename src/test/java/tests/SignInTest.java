package tests;

import base.BaseTest;
import org.p.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SignInTest extends BaseTest {
    private String testEmail;
    private String testPassword;

    public SignInTest() {
        try {
            FileInputStream fis = new FileInputStream("credentials.properties");
            Properties prop = new Properties();
            prop.load(fis);
            this.testEmail = prop.getProperty("testEmail");
            this.testPassword = prop.getProperty("testPassword");
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSignIn() {
        driver.get("https://magento.softwaretestingboard.com/");

        System.out.println("🔹 Attempting to login with:");
        System.out.println("   - Email: " + testEmail);
        System.out.println("   - Password: " + testPassword);

        // Open the Sign In Page
        LoginPage login = new LoginPage(driver);
        login.openSignInPage();
        login.enterCredentials(testEmail, testPassword);
        login.submitLogin();

        // Debug: Print current page title and URL
        System.out.println("🔹 Page Title After Login: " + driver.getTitle());
        System.out.println("🔹 Current URL After Login: " + driver.getCurrentUrl());

        // Verify login success
        boolean loginSuccess = login.isUserLoggedIn();
        System.out.println("🔹 Login Success? " + loginSuccess);

        if (!loginSuccess) {
            System.out.println("Login failed even though credentials are correct.");
        }

        Assert.assertTrue(loginSuccess, "Login failed!");
    }
}
