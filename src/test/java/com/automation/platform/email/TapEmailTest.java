package com.automation.platform.email;

import com.automation.platform.config.Configvariable;
import org.testng.annotations.Test;

import javax.mail.Session;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TapEmailTest {


    private TapEmail tapEmail = new TapEmail();

    public void setGlobalProperties() {
        Configvariable.globalPropertyMap.put("email.smtp.host", "smtp.gmail.com");
        Configvariable.globalPropertyMap.put("email.smtp.socket.port", "465");
        Configvariable.globalPropertyMap.put("email.smtp.socket.class", "javax.net.ssl.SSLSocketFactory");
        Configvariable.globalPropertyMap.put("email.smtp.auth", "true");
        Configvariable.globalPropertyMap.put("email.smtp.port", "465");
        Configvariable.globalPropertyMap.put("email.username", "sgqa@gmail.com");
        Configvariable.globalPropertyMap.put("email.password", "sgqa@123");
        Configvariable.globalPropertyMap.put("email.fromUser", "sgqa@gmail.com");
        Configvariable.globalPropertyMap.put("email.toUser", "testAutomation@gmail.com");
        Configvariable.globalPropertyMap.put("email.subject", "Test Automation Execution Report");
        Configvariable.globalPropertyMap.put("email.message.body", "Please find attached the cucumber report.");
    }


    @Test
    public void testCreateSession() {
        setGlobalProperties();
        Session session = tapEmail.createEmailSession();
    }

//    @Test
//    public void testSendEmail() {
//        String filepath = "target/test-classes/testFile/cucumber.json";
//        setGlobalProperties();
//        tapEmail.sendEmail(filepath);
//    }


}
