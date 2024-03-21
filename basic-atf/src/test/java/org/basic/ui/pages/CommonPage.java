package org.basic.ui.pages;

import org.openqa.selenium.By;

public class CommonPage {
    public static By emailInput = By.xpath("//input[@id='email']");
    public static By passwordInput = By.xpath("//input[@id='password']");
    public By submitButton = By.xpath("//button[@id='submit']");
}
