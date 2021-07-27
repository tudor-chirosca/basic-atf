package com.vocalink.crossproduct.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
                glue = "com.vocalink.crossproduct.component.stepdef",
                plugin = {"pretty", "html:target/cucumber", "json:target/cucumber.json"},
                tags = "not @flaky and not @ignore and not @wip and not @bug")
public class ComponentIT {
}