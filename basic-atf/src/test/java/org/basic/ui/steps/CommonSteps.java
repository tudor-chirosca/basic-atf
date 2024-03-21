package org.basic.ui.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.basic.config.driver.DriverManager;
import org.basic.config.properties.PropertiesManager;
import org.basic.ui.pages.LoginPage;
import org.openqa.selenium.By;

public class CommonSteps {

    @Given("user navigates to {string}")
    public void goToPage(String pageName) {
        DriverManager.getDriver().get(PropertiesManager.getPageName(pageName));
    }

    @When("^user logs in with (correct|incorrect) credentials$")
    public void logInWithCredentials(String word) {
        if (word.equals("correct")) {
            LoginPage.loginWithCorrectCredentials();
        } else {
            LoginPage.loginWithIncorrectCredentials();
        }
    }

    public static void clickButton(By element) {
        DriverManager.getDriver().findElement(element).click();
    }

    public static void enterText(By inputField, String text) {
        DriverManager.getDriver().findElement(inputField).sendKeys(text);
    }
}
