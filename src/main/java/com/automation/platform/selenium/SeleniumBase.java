package com.automation.platform.selenium;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

@Component
public class SeleniumBase {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumBase.class);
    public static WebDriver driver;

    @Autowired
    private ExecuteCommand executeCommand;

    @Autowired
    private ExecutionContext executionContext;

    @Autowired
    Configvariable configvariable;


    public void initializeSeleniumFramework() {
        executionContext.initializeVariables();
    }

    public void launchApplication(String url, String username, String password) {
        String URL = url;
        if (!username.isEmpty() && !password.isEmpty()) {
            String[] parts = url.split("http://");
            URL = "http://" + username + ":" + password + "@" + parts[1];
        }
        driver = executeCommand.getDriverForRequiredBrowser(executionContext.getBrowserType());
        executeCommand.executeCommand(driver, Commands.OPEN_URL, null, URL, "");

    }

    public void quitDriver() {
        executeCommand.executeCommand(driver, Commands.CLOSE_ALL_BROWSER, null, "", "");
    }

    public void setBrowserCookie() {
//        String cookieType = configvariable.getStringVar("web.cookie.type");
        String webCookieName = configvariable.getStringVar("bluegreen.cookie.keys");
        String webCookieValue = configvariable.getStringVar("bluegreen.cookie.values");
        String webCookieDomain = configvariable.getStringVar("bluegreen.cookie.domain");
        if (webCookieValue.isEmpty()) {
            logger.info("Please supply the cookie value for the Key" + webCookieName);
            throw new TapException(TapExceptionType.IO_ERROR, "Please supply the cookie value for the Key [{}]", webCookieName);
        }
        if (webCookieDomain.isEmpty()) {
            logger.info("Please supply the cookie domain value");
            throw new TapException(TapExceptionType.IO_ERROR, "Please supply the cookie domain value");
        }
        StringTokenizer nameToken = new StringTokenizer(webCookieName, ";");
        StringTokenizer valueToken = new StringTokenizer(webCookieValue, ";");
        while (nameToken.hasMoreTokens()) {
            String name = nameToken.nextToken();
            String value = valueToken.nextToken();
            Cookie ck = new Cookie(name, value, webCookieDomain, "/", null, false);
            driver.manage().addCookie(ck);
        }

    }

    public void addDriverCookie(String webCookieName, String webCookieValue, String webCookieDomain) {
        if (webCookieValue.isEmpty()) {
            logger.info("Please supply the cookie value for the Key" + webCookieName);
            throw new TapException(TapExceptionType.IO_ERROR, "Please supply the cookie value for the Key [{}]", webCookieName);
        }
        if (webCookieDomain.isEmpty()) {
            logger.info("Please supply the cookie domain value");
            throw new TapException(TapExceptionType.IO_ERROR, "Please supply the cookie domain value");
        }
        StringTokenizer nameToken = new StringTokenizer(webCookieName, ";");
        StringTokenizer valueToken = new StringTokenizer(webCookieValue, ";");
        while (nameToken.hasMoreTokens()) {
            String name = nameToken.nextToken();
            String value = valueToken.nextToken();
            Cookie ck = new Cookie(name, value, webCookieDomain, "/", null, false);
            driver.manage().addCookie(ck);
        }
    }


    public void deleteDriverCookie(String cookieName) {
        logger.info("Cookie name is : " + cookieName);
        driver.manage().deleteCookieNamed(cookieName);
    }

}
