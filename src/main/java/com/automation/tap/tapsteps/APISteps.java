package com.automation.tap.tapsteps;

import com.automation.tap.cucumberUtils.ScenarioUtils;
import com.automation.tap.filehandling.FileReaderUtil;
import com.automation.tap.api.GraphQlApi;
import com.automation.tap.api.HttpClientApi;
import com.automation.tap.config.Configvariable;
import com.automation.tap.config.TapBeansLoad;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class APISteps {
    private static final Logger logger = Logger.getLogger(APISteps.class);
    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private HttpClientApi httpClientApi = (HttpClientApi) TapBeansLoad.getBean(HttpClientApi.class);
    private ScenarioUtils scenarioUtils = (ScenarioUtils) TapBeansLoad.getBean(ScenarioUtils.class);

    @When("I create connection for api service")
    public void createConnection() {
        httpClientApi.createHttpClient1();
    }

    @Given("^I set endpoint url as \"([^\"]*)\"$")
    public void setEndpointURL(String url) {
        String endPoint = configvariable.expandValue(url);
        scenarioUtils.write("API Url is :" + endPoint);
        httpClientApi.setUrl(endPoint);
    }

    @Given("^I set api header key \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void setHeaders(String key, String value) {
        String headerKey = configvariable.expandValue(key);
        String headerValue = configvariable.expandValue(value);
        scenarioUtils.write("API header key is :" + headerKey + " value is :" + headerValue);
        httpClientApi.setSendHeaders(headerKey, headerValue);
    }

    @When("^I set api headers as below$")
    public void setApiHeaders(DataTable headers) {
        Map<String, String> variableMap;
        variableMap = headers.asMap(String.class, String.class);
        scenarioUtils.write("API headers are :" + variableMap);
        httpClientApi.setSendHeaders(variableMap);
    }

    @Given("^I set multipart key \"([^\"]*)\" as file \"([^\"]*)\"$")
    public void addMultipartFile(String key, String filePath) {
        String headerKey = configvariable.expandValue(key);
        String headerValue = configvariable.expandValue(filePath);
        logger.info("Set multipart key as  " + headerKey + " and value as " + headerValue);
        scenarioUtils.write("Set multipart key as  " + headerKey + " and value as " + headerValue);
        String path = configvariable.getClasspath(configvariable.expandValue(filePath));
        File file = new File(path);
        httpClientApi.addMultiPartAsFile(headerKey, file);
    }

    @Given("^I set multipart key \"([^\"]*)\" as text \"([^\"]*)\"$")
    public void addMultipartText(String key, String filePath) {
        String headerKey = configvariable.expandValue(key);
        String headerValue = configvariable.expandValue(filePath);
        logger.info("Set multipart key as  " + headerKey + " and value as " + headerValue);
        scenarioUtils.write("Set multipart key as  " + headerKey + " and value as " + headerValue);
        httpClientApi.addMultiPartAsText(headerKey, headerValue);
    }

    @When("^I set request body as below$")
    public void setRequestBody(String requestBody) {
        String formattedRequestBody = configvariable.expandValue(requestBody);
        scenarioUtils.write("Request body is :" + formattedRequestBody);
        httpClientApi.setRequestBody(formattedRequestBody);
    }

    @When("^I set request body from file \"([^\"]*)\"$")
    public void setRequestBodyFromFile(String fileInputStreamPath) {
        String filePath = configvariable.expandValue(fileInputStreamPath);
        InputStream initialStream = getClass().getResourceAsStream(filePath);
        String fileValue = FileReaderUtil.convertFileToString(initialStream);
        scenarioUtils.write("Request body is :" + fileValue);
        httpClientApi.setRequestBody(configvariable.expandValue(fileValue));
    }

    @When("^I set graphql request body from file \"([^\"]*)\"$")
    public void setGraphQlRequestBody(String fileInputStreamPath) {
        String filePath = configvariable.expandValue(fileInputStreamPath);
        InputStream initialStream = getClass().getResourceAsStream(filePath);
        String fileValue = GraphQlApi.parseGraphql(initialStream, null, "query");
        scenarioUtils.write("Request body is :" + fileValue);
        httpClientApi.setRequestBody(configvariable.expandValue(fileValue));
    }

    @When("^I send post request to api$")
    public void sendPostRequest() {
        logger.info("Sending post request");
        httpClientApi.createHttpPost();
        httpClientApi.setBodyParameters();
        httpClientApi.getHTTPPostResponse();
        scenarioUtils.write("Response body is :" + httpClientApi.getResponseBody());
        httpClientApi.resetVariables();
    }

    @When("^I send get request to api$")
    public void sendGetRequest() {
        logger.info("Sending get request");
        httpClientApi.createHttpGet();
        httpClientApi.setBodyParameters();
        httpClientApi.getHTTPGetResponse();
        scenarioUtils.write("Response body is :" + httpClientApi.getResponseBody());
        httpClientApi.resetVariables();
    }

    @When("^I verify response code is (.*)$")
    public void verifyStatusCode(int resCode) {
        int responseCode = httpClientApi.getResponseCode();
        scenarioUtils.write("Actual code is :" + Integer.toString(responseCode));
        scenarioUtils.write("Expected code is :" + resCode);
        Assert.assertEquals("Response code should be " + resCode, resCode, responseCode);
    }

    @When("^I verify response value for node \"([^\"]*)\" is \"([^\"]*)\"$")
    public void verifyResponseData(String jsonPath, String value) {
        String jsonP = configvariable.expandValue(jsonPath);
        String jsonVal = configvariable.expandValue(value);
        String responseVal = httpClientApi.getJsonPathStringValue(jsonP);
        scenarioUtils.write("Expected value for :" + jsonP + " is " + jsonVal);
        scenarioUtils.write("Actual value for :" + jsonP + " is " + responseVal);
        Assert.assertEquals(jsonVal, responseVal);
    }

    @When("^I verify response count for node \"([^\"]*)\" is (.*)$")
    public void verifyResponseDataCount(String jsonPath, int value) {
        String jsonP = configvariable.expandValue(jsonPath);
        int responseCount = httpClientApi.getJsonPathListValue(jsonP).size();
        scenarioUtils.write("Actual code is :" + responseCount);
        scenarioUtils.write("Expected code is :" + value);
        Assert.assertEquals(value, responseCount);
    }

    @When("^I get response value for node \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getResponseNodeData(String jsonPath, String variable) {
        String jsonP = configvariable.expandValue(jsonPath);
        String responseVal = httpClientApi.getJsonPathStringValue(jsonP);
        scenarioUtils.write("Response value for is :" + jsonPath + " is " + responseVal);
        configvariable.setStringVariable(responseVal, variable);
    }

    @When("I close connection for api service")
    public void closeConnection() {
        httpClientApi.closeHttpClent();
    }

    @When("^I send request \"([^\"]*)\" to api$")
    public void sendMultipartPostRequest(String method) {
        logger.info("Sending multipart " + method + " request");
//        httpClientApi.createHttpClient1();
        httpClientApi.executeRequestAndGetResponse(method);
        scenarioUtils.write("Response body is :" + httpClientApi.getResponseBody());
        httpClientApi.resetVariables();
    }

    @When("^I set multipart data as below$")
    public void setMultipartData(DataTable data) {
        Map<String, String> variableMap;
        variableMap = data.asMap(String.class, String.class);
        scenarioUtils.write("Multipart data are :" + variableMap);

        for (String key : variableMap.keySet()) {
            String headerKey = configvariable.expandValue(key);
            String headerValue = configvariable.expandValue(variableMap.get(key));
            httpClientApi.addMultiPartAsText(headerKey, headerValue);
        }
    }

}
