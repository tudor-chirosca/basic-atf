package com.vocalink.crossproduct.component.stepdef;

import com.vocalink.crossproduct.component.ScenarioContext;
import com.vocalink.crossproduct.component.mock.BpsServiceMock;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ScenarioSetup {

    @Autowired
    ScenarioContext context;

    @Autowired
    BpsServiceMock bpsServiceMock;

    @Before
    public void before(Scenario scenario) {
        log.info("==============================================================================");
        log.info(scenario.getName());
        log.info("==============================================================================");
        context.clearState();
    }

    @After
    public void after() {
        context.clearState();
        bpsServiceMock.reset();
    }
}
