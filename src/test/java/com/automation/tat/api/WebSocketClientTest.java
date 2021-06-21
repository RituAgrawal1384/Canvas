//package com.automation.tap.api;

//import java.util.Map;
//import org.apache.log4j.Logger;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.testng.annotations.BeforeSuite;
//import org.testng.annotations.Test;
//
//import static org.testng.Assert.assertNotNull;
//
//@ComponentScan(basePackages = {"com.automation.tap"})
//@Configuration
//public class WebSocketClientTest {
//    private static final Logger logger = Logger.getLogger(WebSocketClientTest.class);
//    private WebSocketClient webSocketClient;
//
//    private JsonReader jsonReader;
//
//    private Configvariable configvariable;
//
//    public static ConfigurableApplicationContext context;
//
//    @BeforeSuite
//    public void setup() {
//        if (context == null) {
//            context = new AnnotationConfigApplicationContext(WebSocketClientTest.class);
//            jsonReader = context.getBean(JsonReader.class);
//            webSocketClient = context.getBean(WebSocketClient.class);
//            configvariable = context.getBean(Configvariable.class);
//        }
//    }
//
//    @Test
//    public void testSendRequestAndCheckForResponse() {
//        String URL = "type_socket_url";
//        configvariable.setStringVariable("sgqa+1510151024@gmail.com", "USER_NAME");
//        configvariable.setStringVariable("Pas$1234", "USER_PASSWORD");
//        webSocketClient.setWebSocketURL(URL);
//        String response = webSocketClient.sendRequestAndCheckForResponse("/testFile/Login.json");
//        logger.info("Receiving: " + response);
//        assertNotNull(response);
//        webSocketClient.disconnectToWebSocket();
//    }
//
//    @Test
//    public void testSendMalinatorRequestAndGetForResponse() {
//        //taf-249071111135@mailinator.com
//        String MalinatorURL = "wss://www.mailinator.com/ws/fetchinbox?zone=public&query=taf-249071111135";
//        configvariable.setStringVariable("Account creation request", "EMAIL_SUBJECT");
//        String response = webSocketClient.sendMalinatorRequestAndGetForResponse(MalinatorURL);
//        Map<String, Object> responseMap = jsonReader.convertJsonStringToMap(response);
//        assertNotNull(responseMap);
//        webSocketClient.disconnectToWebSocket();
//    }
//
//}
