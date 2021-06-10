package com.prudential.tap.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.InputStream;

import com.prudential.tap.config.Configvariable;
import com.prudential.tap.config.TapBeansLoad;
import com.prudential.tap.database.DataBaseMethodsTest;
import com.prudential.tap.database.DatabaseMethods;
import com.prudential.tap.filehandling.CsvUtils;
import com.prudential.tap.filehandling.FileReaderUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@ComponentScan(basePackages = {"com.prudential.tap"})
@Configuration
public class GraphQlApiTest {

    private HttpClientApi httpClientApi;

    private Configvariable configvariable;

    @BeforeSuite
    public void setup(){
        TapBeansLoad.setConfigClass(DataBaseMethodsTest.class);
        TapBeansLoad.init();
        configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
        httpClientApi = (HttpClientApi) TapBeansLoad.getBean(HttpClientApi.class);
    }

    @Test
    public void testparseGraphqlWithOutVariable() {
        String filepath = "target/test-classes/testFile/graphql/decodeToken.graphql";
        File file = new File(filepath);
        String graphqlPayload = GraphQlApi.parseGraphql(file, null, "query");
        Assert.assertNotNull(graphqlPayload);
    }

    @Test
    public void testparseGraphqlWithVariable() {
        String filepath = "target/test-classes/testFile/graphql/decodeToken_withvar.graphql";
        File file = new File(filepath);
        // Create a variables to pass to the graphql query
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("jwttoken", "Test");
        String graphqlPayload = GraphQlApi.parseGraphql(file, variables, "query");
        Assert.assertNotNull(graphqlPayload);
    }

//    @Test
//    public void testApis() {
//        httpClientApi.createHttpClient1();
//        httpClientApi.setUrl("https://uat-api.eb.prudential.com.sg/graphql/sales-portal");
//        httpClientApi.setSendHeaders("fullstackdeployheader", "blue");
//        httpClientApi.setSendHeaders("content-type", "application/json");
//        InputStream initialStream = getClass().getResourceAsStream("/testFile/graphql/login.graphql");
//        String fileValue = GraphQlApi.parseGraphql(initialStream, null, "query");
////        String fileValue = FileReaderUtil.convertFileToString(initialStream);
//        httpClientApi.setRequestBody(fileValue);
//        httpClientApi.createHttpPost();
//        httpClientApi.setBodyParameters();
//        httpClientApi.getHTTPPostResponse();
//        httpClientApi.closeHttpClent();
//        Assert.assertEquals(httpClientApi.getResponseCode(), 200);
//        String token = httpClientApi.getJsonPathStringValue("data.login.token");
//        Assert.assertEquals(httpClientApi.getResponseCode(), 200);
//    }


}
