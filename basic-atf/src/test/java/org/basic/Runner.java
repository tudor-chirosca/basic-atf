package org.basic;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "org/basic/hooks/Hooks.java",
                "json:reports/cucumber-report/cucumber.json"
        },
        tags = "@UI",
        features = {"src/test/resources/features"},
        glue = "org.basic"
)
public class Runner {
}
