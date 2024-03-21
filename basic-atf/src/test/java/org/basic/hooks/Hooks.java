package org.basic.hooks;

import org.basic.config.driver.DriverManager;
//import org.junit.After;
//import org.junit.Before;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    @Before
    public void setUp() {
        DriverManager.getDriver();
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
