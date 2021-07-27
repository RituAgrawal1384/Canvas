package com.automation.platform.selenium;

import com.automation.platform.driver.TapDriver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ExecuteCommand {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteCommand.class);

    @Autowired
    private TapDriver tapDriver;

    public boolean executeCommand(WebDriver driver, Commands commands, Robot robot, String locator, String value) {
        return commands.executeCommand(driver, robot, locator, value);
    }

    public WebDriver getDriverForRequiredBrowser(String browserType) {
        WebDriver driver = null;
        switch (browserType.toLowerCase()) {
            case "ie":
                driver = tapDriver.createIEDriver();
                break;
            case "chrome":
                driver = tapDriver.createChromeDriver();
                break;
            case "firefox":
                driver = tapDriver.createFirefoxDriver();
                break;
            case "phantom":
                driver = tapDriver.createPhantomJsDriver();
                break;
            case "safari":
                driver = tapDriver.createSafariDriver();
                break;
            case "edge":
                driver = tapDriver.createEdgeDriver();
                break;
        }
        return driver;
    }

}
