package org.basic.utils;

import org.basic.config.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waiters {

    public static boolean isElementDisplayed(By elementLocator, Duration timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeoutInSeconds);
        WebElement element = null;
        try {
             element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
            return element.isDisplayed();
        } catch (Exception e) {
            assert element != null;
            return !element.isDisplayed();
        }
    }
}
