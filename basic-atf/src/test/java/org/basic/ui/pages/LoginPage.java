package org.basic.ui.pages;

import org.basic.config.properties.PropertiesManager;
import org.basic.ui.steps.CommonSteps;
import org.basic.utils.Waiters;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class LoginPage extends CommonPage {

    private static By signUpButton = By.xpath("//button[@id='signup']");
    private By errorLabel = By.xpath("//*[@id='error']");

    private static boolean isDisplayed = Waiters.isElementDisplayed(emailInput, PropertiesManager.checkElementIsDisplayedTimeout());

    public static void loginWithCorrectCredentials() {
        if (isDisplayed) {
            CommonSteps.enterText(emailInput, PropertiesManager.getUsername());
            CommonSteps.enterText(passwordInput, PropertiesManager.getPassword());
            CommonSteps.clickButton(signUpButton);
        }
    }

    public static void loginWithIncorrectCredentials() {
        Waiters.isElementDisplayed(emailInput, PropertiesManager.checkElementIsDisplayedTimeout());
        CommonSteps.enterText(emailInput, PropertiesManager.getUsername());
        CommonSteps.enterText(passwordInput, PropertiesManager.getPassword());
        CommonSteps.clickButton(signUpButton);
    }
}
