package com.automation.platform.api;

import com.automation.platform.database.DataBaseMethodsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;

import com.automation.platform.config.Configvariable;
import com.automation.platform.config.TapBeansLoad;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@ComponentScan(basePackages = {"com.automation.tap"})
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


}
