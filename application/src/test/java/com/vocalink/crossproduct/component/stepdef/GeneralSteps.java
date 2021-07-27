package com.vocalink.crossproduct.component.stepdef;

import static org.assertj.core.api.Assertions.assertThat;

import com.vocalink.crossproduct.component.ScenarioContext;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class GeneralSteps {

    @Autowired
    ScenarioContext context;

    @Then("^the system returns status code (\\d+)$")
    public void responseStatusCodeIs(int statusCode) throws Exception {
        assertThat(statusCode).isEqualTo(context.getLastResponse().getStatusCode().value());
    }

}
