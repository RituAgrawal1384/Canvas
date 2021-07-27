package com.automation.platform.ui;

import com.automation.platform.appium.AppiumCommands;
import com.automation.platform.config.Configvariable;
import com.automation.platform.driver.TapDriver;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.automation.platform.selenium.*;
import cucumber.api.Scenario;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UiBasePage {
    private static final Logger logger = LoggerFactory.getLogger(UiBasePage.class);

    @Autowired
    private Configvariable configvariable;

    @Autowired
    private SeleniumBase seleniumBase;

    @Autowired
    private ExecuteCommand executeCommand;

    @Autowired
    private JavaScriptExec javaScriptExec;

    @Autowired
    private WebTableMethods webTableMethods;

    @Autowired
    private TapDriver tapDriver;

    @Autowired
    private AppiumCommands appiumCommands;

    private WebDriver driver;

    private String platform;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


    public void launchApp(String url, String userName, String password) {
        logger.info("Launching application : " + url);
        seleniumBase.launchApplication(url, userName, password);
        driver = SeleniumBase.driver;
    }

    public void refreshBrowser() {
        logger.info("Refreshing browser");
        executeCommand.executeCommand(driver, Commands.REFRESH_BROWSER, null, "", "");
    }

    public void navigateURL(String url) {
        logger.info("Navigating to url : " + url);
        executeCommand.executeCommand(driver, Commands.NAVIGATE_TO, null, url, "");
    }

    public void clickElement(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Clicking on element : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.CLICK, null, formattedLocator, "");
    }

    public void clearElementText(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Clearing text for element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.CLEAR, null, formattedLocator, "");
    }

    public void enterText(String locator, String text) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Enter text on element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.CLEAR, null, formattedLocator, "");
        executeCommand.executeCommand(driver, Commands.SEND_KEYS, null, formattedLocator, text);
    }

    public void enterTextWithTabOut(String locator, String text) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Enter text on element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.CLEAR, null, formattedLocator, "");
        executeCommand.executeCommand(driver, Commands.SEND_KEYS_TAB, null, formattedLocator, text);
    }

    public String getElementText(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Get text for element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.GET_TEXT, null, formattedLocator, "");
        return configvariable.getStringVar("element.text");
    }

    public boolean isElementDisplayed(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        return executeCommand.executeCommand(driver, Commands.IS_ELEMENT_DISPLAYED, null, formattedLocator, "");
    }

    public void clickButtonUsingJs(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Clicking on element : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        javaScriptExec.clickElementJS(driver, formattedLocator);
    }

    public void waitTime(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (Exception e) {

        }
    }

    public boolean isElementSelected(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        return executeCommand.executeCommand(driver, Commands.IS_ELEMENT_SELECTED, null, formattedLocator, "");
    }

    public String getAttributeValue(String locator, String attributeName) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Get attribute for element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.GET_ATTRIBUTE, null, formattedLocator, attributeName);
        return configvariable.getStringVar("element.attribute");
    }

    public boolean isElementEnabled(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        return executeCommand.executeCommand(driver, Commands.IS_ELEMENT_ENABLED, null, formattedLocator, "");
    }

    public String getParentWindowHandle() {
        executeCommand.executeCommand(driver, Commands.GET_PARENT_WINDOW_HANDLE, null, "", "");
        logger.info("parent window id is: " + configvariable.getStringVar("parent.window.handle"));
        return configvariable.getStringVar("parent.window.handle");
    }

    public void switchToParentWindow(String windowHandle) {
        executeCommand.executeCommand(driver, Commands.SWITCH_TO_WINDOW_HANDLE, null, windowHandle, "");
    }

    public void scrollWebPageUP() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)", new Object[0]);
        waitTime(5);
    }

    public void scrollWebPageDown() {
        javaScriptExec.scrollPage(driver);
        waitTime(5);
    }

    public void closeBrowser() {
        logger.info("Close Browser");
        executeCommand.executeCommand(driver, Commands.CLOSE_BROWSER, null, "", "");
    }

    public void closeAllBrowser() {
        logger.info("Close all browsers");
        executeCommand.executeCommand(driver, Commands.CLOSE_ALL_BROWSER, null, "", "");
    }

    public void selectCheckbox(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Select checkbox : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, locator, "10");
        if (executeCommand.executeCommand(driver, Commands.IS_ELEMENT_SELECTED, null, locator, "") == false) {
            clickButtonUsingJs(locator);
        }
    }

    public void unSelectCheckbox(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("UnSelect checkbox : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, locator, "10");
        if (executeCommand.executeCommand(driver, Commands.IS_ELEMENT_SELECTED, null, locator, "") == true) {
            clickButtonUsingJs(locator);
        }
    }

    public String getBrowserTitle() {
        executeCommand.executeCommand(driver, Commands.GET_TITLE, null, "", "");
        return configvariable.getStringVar("driver.title");
    }

    public String getCurrentURL() {
        executeCommand.executeCommand(driver, Commands.GET_CURRENT_URL, null, "", "");
        return configvariable.getStringVar("driver.current.url");
    }

    public void scrollBrowserTillElement(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Scroll browser");
        javaScriptExec.scrollWebPageTillElementVisible(driver, formattedLocator);
    }

    public void uploadFileToBrowser(String filePath, String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Upload file : " + filePath);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.SEND_KEYS, null, formattedLocator, filePath);
    }

    public String getElementColorAttribute(String locator, String attributeName) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Get color for element: " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        String color = driver.findElement(By.xpath(formattedLocator)).getCssValue(attributeName);
        return Color.fromString(color).asRgb();
    }

    public void mouseHoverAndClickOnElement(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Clicking on element : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.MOUSE_HOVER_AND_CLICK, null, formattedLocator, "");
    }

    public void mouseHoverOnElement(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Mouse Hover on element : " + formattedLocator);
        executeCommand.executeCommand(driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, formattedLocator, "10");
        executeCommand.executeCommand(driver, Commands.MOUSE_HOVER, null, formattedLocator, "");
    }

    public void scrollTillCoordinate(String xCord, String yCord) {
        logger.info("Scroll element by coordinates X: " + xCord + ",Y:" + yCord);
        String windowScrollScript = "window.scrollBy(" + xCord + "," + yCord + ")";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(windowScrollScript, new Object[0]);
    }

    public void switchToChildWindow() {
        executeCommand.executeCommand(driver, Commands.GET_CHILD_WINDOW_HANDLE, null, "", "");
        String childWindow = configvariable.getStringVar("child.window.handle");
        logger.info("child window id is: " + childWindow);
        executeCommand.executeCommand(driver, Commands.SWITCH_TO_WINDOW_HANDLE, null, childWindow, "");
    }

    public String getAlertText() {
        logger.info("Get alert text ");
        executeCommand.executeCommand(driver, Commands.GET_ALERT_TEXT, null, "", "");
        return configvariable.getStringVar("alert.text");
    }

    public void acceptAlert() {
        executeCommand.executeCommand(driver, Commands.ACCEPT_ALERT, null, "", "");
    }

    public void switchFrameByID(String frameId) {
        logger.info("Switch to frame : " + frameId);
        executeCommand.executeCommand(driver, Commands.SWITCH_TO_FRAME, null, frameId, "");
    }

    public void switchFrameByName(String frameName) {
        logger.info("Switch to frame : " + frameName);
        executeCommand.executeCommand(driver, Commands.SWITCH_TO_FRAME, null, frameName, "");
    }

    public void switchToDefaultWindow() {
        executeCommand.executeCommand(driver, Commands.SWITCH_TO_PARENT_FRAME, null, "", "");
    }

    public int getTableRowCount(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        int rows = webTableMethods.getRowsCountInTable(driver, formattedLocator);
        logger.info("Rows count is : " + rows);
        return rows;
    }

    public int getTableColumnCount(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        int cols = webTableMethods.getColumnsCountInTable(driver, formattedLocator);
        logger.info("Columns count is : " + cols);
        return cols;
    }

    public String getTableCellData(String locator, String row, String col) {
        String formattedLocator = configvariable.expandValue(locator);
        String cellData = webTableMethods.getTDCellValue(driver, formattedLocator, Integer.parseInt(row), Integer.parseInt(col));
        logger.info("Table cell value is : " + cellData);
        return cellData;
    }

    public int getTableColumnIndex(String locator, String columnName) {
        String formattedLocator = configvariable.expandValue(locator);
        Map<String, Integer> columnsIndex = new HashMap();
        List<WebElement> col = driver.findElements(By.xpath(formattedLocator));

        for (int iCount = 0; iCount < col.size(); ++iCount) {
            columnsIndex.put(((WebElement) col.get(iCount)).getText(), iCount + 1);
        }

        return columnsIndex.get(columnName);
    }

    public List<String> getTableHeaders(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        List<String> tableHeadersList = new ArrayList();
        List<WebElement> col = driver.findElements(By.xpath(formattedLocator));
        for (int iCount = 0; iCount < col.size(); ++iCount) {
            String header = ((WebElement) col.get(iCount)).getText();
            if (!header.isEmpty()) {
                tableHeadersList.add(((WebElement) col.get(iCount)).getText());
            }
        }
        return tableHeadersList;
    }

    public void launchMobileApplication(String platform) throws MalformedURLException {
        String os = configvariable.expandValue(platform);
        logger.info("Launching mobile application for : " + os);
        if (os.equalsIgnoreCase("android")) {
            tapDriver.androidDriver("", Configvariable.envPropertyMap.get("android.app.package"), Configvariable.envPropertyMap.get("android.app.activity"));
            logger.info("Android driver created");

        } else if (os.equalsIgnoreCase("iOS")) {
            tapDriver.iOSDriver("", Configvariable.envPropertyMap.get("ios.app.bundle.id"), Configvariable.envPropertyMap.get("ios.app.udid"), Configvariable.envPropertyMap.get("ios.app.platform.version"));
            logger.info("iOS driver created");
        }
        driver = tapDriver.getWebDriver();
        appiumCommands.setDriver(driver);
    }

    public String getDevicePlatform() {
        if (System.getenv("DEVICEFARM_DEVICE_PLATFORM_NAME") != null) {
            platform = System.getenv("DEVICEFARM_DEVICE_PLATFORM_NAME");
        } else {
            platform = System.getProperty("osType");
        }
        return platform;
    }

    public void swipeMobileDown(String duration) {
        appiumCommands.swipe(AppiumCommands.DIRECTION.DOWN, Long.parseLong(duration));
    }

    public void swipeMobileUp(String duration) {
        appiumCommands.swipe(AppiumCommands.DIRECTION.UP, Long.parseLong(duration));
    }

    public void swipeMobileDownTillElementVisible(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        int count = 9;
        Dimension size = appiumCommands.getDriver().manage().window().getSize();
        while (!isElementDisplayed(formattedLocator) && count >= 0) {
            appiumCommands.swipe(AppiumCommands.DIRECTION.DOWN, 100);
            count--;
        }
    }

    public void swipeMobileUpTillElementVisible(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        int count = 9;
        while (!isElementDisplayed(formattedLocator) && count >= 0) {
            appiumCommands.swipe(AppiumCommands.DIRECTION.UP, 100);
            count--;
        }
    }

    public void addMobileCapability(String capabilityKey, String capabilityVal) {
        tapDriver.capabilities.setCapability(capabilityKey, capabilityVal);
    }

    public void hideKeyboard() {
        appiumCommands.hideKeyboard();
    }

    public void embedScenarioScreenshot(Scenario scenario) {
        if (driver != null) {
            try {
                final byte[] screenshot1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot1, "image/png");
            } catch (Exception ex) {
                logger.info("Failed to take screenshot of ");
                throw new TapException(TapExceptionType.PROCESSING_FAILED, "Failed to take screenshot of ", ex.getMessage());
            }
        } else {
            logger.info("Driver is null, can't take screenshot");
        }
    }

}
