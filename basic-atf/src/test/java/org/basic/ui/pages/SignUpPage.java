package org.basic.ui.pages;

import org.openqa.selenium.By;

public class SignUpPage extends CommonPage {
    private By loginLabel = By.xpath("//*[text()='Log In:']");
    private By firstNameInput = By.xpath("//input[@id='firstName']");
    private By lastNameInput = By.xpath("//input[@id='lastName']");
    private By cancelButton = By.xpath("//button[@id='cancel']");
}
