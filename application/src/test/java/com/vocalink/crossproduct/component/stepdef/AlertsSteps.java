package com.vocalink.crossproduct.component.stepdef;

import com.vocalink.crossproduct.component.ScenarioContext;
import com.vocalink.crossproduct.component.generator.TestMessageGenerator;
import com.vocalink.crossproduct.component.mock.BpsServiceMock;
import com.vocalink.crossproduct.component.model.test.TestMockData;
import com.vocalink.crossproduct.component.persistence.AuditDetailsBuilder;
import com.vocalink.crossproduct.component.rest.TestRestClient;
import com.vocalink.crossproduct.component.rest.model.PathResolver;
import com.vocalink.crossproduct.component.util.JsonUtil;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class AlertsSteps {

    @Autowired
    TestRestClient restClient;

    @Autowired
    PathResolver pathResolver;

    @Autowired
    ScenarioContext context;

    @Autowired
    TestMessageGenerator msgGenerator;

    @Autowired
    BpsServiceMock bpsServiceMock;

    @Autowired
    JsonUtil jsonUtil;

    @Autowired
    AuditDetailsBuilder auditDetailsBuilder;

    @Given("ISS is up and running")
    public void isUpAndRunning(){}

    @Given("existing {string} in BPS")
    public void existingRefAlertInBps(String dataType) throws Exception {
        String json = msgGenerator.createMessageBody(dataType);
        bpsServiceMock.acceptPostRequestsOnPath(pathResolver.getBpsPath(dataType), json);
        context.putScenarioResource(TestMockData.class, new TestMockData(json));
    }

    @Given("a valid fetch {string} request")
    public void createValidAlertsRequest(String dataType) throws Exception {
        String json = createRequestBody(dataType, Optional.empty());
    }

    @Given("user details")
    public void givenUserDetails() {
        auditDetailsBuilder.saveAuditDetails();
    }

    @When("ISS receives a request on {string} endpoint")
    public void getAlertsSummary(String issEndpoint){
        //make rest call on iss endpoint
        ResponseEntity<?> response = restClient.get(pathResolver.getIssPath(issEndpoint));
        context.addResponse(response);
    }

    @When("ISS receives a request on {string} endpoint with query params:")
    public void issReceiveRequest(String pathName, DataTable dataTable) {
        ResponseEntity<?> response = restClient.get(pathResolver.getIssPath(pathName), dataTable.asMap(String.class, String.class));
        log.info("response: " + response.getBody());
    }

    @Then("ISS returns all alerts references")
    public void assertAlertsReferences() throws Exception {
        String actual = jsonUtil.makePretty(context.getLastResponse().getBody().toString());
        String expected = jsonUtil.makePretty(context.getScenarioResource(TestMockData.class).getMockData());

        assertThat(jsonUtil.areEqual(actual, expected))
                        .withFailMessage("Actual: " + actual + "\n" + "has not matched" + "\n" + "Expected: " + expected)
                        .isTrue();
    }

    private String createRequestBody(String requestBodyTemplate, Optional<Map<String, String>> scenarioData) throws Exception {
        return scenarioData.isPresent()
                        ? msgGenerator.createMessageBody(requestBodyTemplate, scenarioData.get())
                        : msgGenerator.createMessageBody(requestBodyTemplate);
    }

}
