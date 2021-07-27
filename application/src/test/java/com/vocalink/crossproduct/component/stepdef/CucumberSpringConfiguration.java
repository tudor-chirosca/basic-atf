package com.vocalink.crossproduct.component.stepdef;

import com.vocalink.crossproduct.InternationalSuiteServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureWireMock() //(addFilters = false)
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = InternationalSuiteServiceApplication.class)
@ActiveProfiles("component")
public class CucumberSpringConfiguration {

}
