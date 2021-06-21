package com.automation.tat.tapsteps;

import com.automation.tat.cucumberUtils.ScenarioUtils;
import com.automation.tat.selenium.SeleniumBase;
import com.automation.tat.config.Configvariable;
import com.automation.tat.config.TapBeansLoad;
import com.automation.tat.ui.UiBasePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.util.List;

public class UISteps {
    private static final Logger logger = Logger.getLogger(UISteps.class);

    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private SeleniumBase seleniumBase = (SeleniumBase) TapBeansLoad.getBean(SeleniumBase.class);
    private UiBasePage uiBasePage = (UiBasePage) TapBeansLoad.getBean(UiBasePage.class);
    private ScenarioUtils scenarioUtils = (ScenarioUtils) TapBeansLoad.getBean(ScenarioUtils.class);

    @Given("^I set browser type as \"([^\"]*)\"$")
    public void setBrowserType(String browser) {
        logger.info("Browser is : " + browser);
        scenarioUtils.write("Browser is : " + browser);
        configvariable.setStringVariable(browser, "web.browser.type");
    }

    @Given("^I open browser in hidden mode \"([^\"]*)\"$")
    public void setBrowserMode(String browserMode) {
        logger.info("Browser mode is : " + browserMode);
        scenarioUtils.write("Browser mode is : " + browserMode);
        configvariable.setStringVariable(browserMode, "web.driver.headless");
    }

    @Given("^I set default download directory as \"([^\"]*)\"$")
    public void setDefaultDownloadDir(String downloadDir) {
        logger.info("Download directory is : " + downloadDir);
        scenarioUtils.write("Download directory is : " + downloadDir);
        configvariable.setStringVariable(downloadDir, "web.browser.download");
    }

    @Given("^Initialize selenium framework$")
    public void initializeSel() {
        logger.info("Initializing selenium");
        seleniumBase.initializeSeleniumFramework();
    }


    @Given("^I launch browser application \"([^\"]*)\"$")
    public void launchBrowserApplication(String url) {
        scenarioUtils.write("Web url is " + url);
        uiBasePage.launchApp(url, "", "");
    }

    @Given("^I launch browser application \"([^\"]*)\" with browser authentication username \"([^\"]*)\" and \"([^\"]*)\" password$")
    public void launchBrowserApplication(String url, String userName, String password) {
        scenarioUtils.write("Web url is " + url);
        uiBasePage.launchApp(url, userName, password);
    }

    @Given("^I set browser cookie name \"([^\"]*)\" and value \"([^\"]*)\" for domain \"([^\"]*)\"$")
    public void setBrowserCookie(String cookieName, String cookieValue, String domain) {
        logger.info("Cookie name is : " + cookieName);
        logger.info("Cookie value is : " + cookieValue);
        logger.info("Cookie domain is : " + domain);
        scenarioUtils.write("Cookie name is : " + cookieName);
        scenarioUtils.write("Cookie value is : " + cookieValue);
        scenarioUtils.write("Cookie domain is : " + domain);
        seleniumBase.addDriverCookie(configvariable.expandValue(cookieName), configvariable.expandValue(cookieValue), configvariable.expandValue(domain));
    }

    @Given("^I delete browser cookie name \"([^\"]*)\"$")
    public void deleteBrowserCookie(String cookieName) throws InterruptedException {
        scenarioUtils.write("Deleting Cookie : " + cookieName);
        seleniumBase.deleteDriverCookie(configvariable.expandValue(cookieName));
        waitTime(10);
    }

    @Given("^I navigate to url \"([^\"]*)\"$")
    public void navigateToURL(String url) {
        scenarioUtils.write("Navigate url : " + url);
        uiBasePage.navigateURL(configvariable.expandValue(url));
    }

    @Given("^I refresh browser$")
    public void refreshBrowser(String url) {
        uiBasePage.refreshBrowser();
    }

    @Given("^I click on element \"([^\"]*)\"$")
    public void clickElement(String locator) {
        uiBasePage.clickElement(locator);
    }

    @Given("^I clear text on element \"([^\"]*)\"$")
    public void clearText(String locator) {
        uiBasePage.clearElementText(locator);
    }

    @Given("^I enter text \"([^\"]*)\" on element \"([^\"]*)\"$")
    public void enterText(String text, String locator) {
        uiBasePage.enterText(locator, configvariable.expandValue(text));
    }

    @Given("^I enter text \"([^\"]*)\" on element \"([^\"]*)\" with tab$")
    public void enterTextWithTab(String text, String locator) {
        uiBasePage.enterTextWithTabOut(locator, configvariable.expandValue(text));
    }

    @Given("^I get text of element \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getElementText(String locator, String variable) {
        configvariable.setStringVariable(uiBasePage.getElementText(locator), variable);

    }

    @Given("^I verify element \"([^\"]*)\" is displayed$")
    public void verifyElementDisplayed(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is displayed: " + formattedLocator);
        Assert.assertTrue(formattedLocator + "Element is displayed", uiBasePage.isElementDisplayed(formattedLocator));

    }

    @Given("^I verify element \"([^\"]*)\" is not displayed$")
    public void verifyElementNotDisplayed(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is not displayed: " + formattedLocator);
        Assert.assertFalse(formattedLocator + "Element is not displayed", uiBasePage.isElementDisplayed(formattedLocator));

    }

    @Given("^I verify element \"([^\"]*)\" is enabled$")
    public void verifyElementEnabled(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is enabled: " + formattedLocator);
        Assert.assertTrue(formattedLocator + "Element is enabled", uiBasePage.isElementEnabled(formattedLocator));

    }

    @Given("^I verify element \"([^\"]*)\" is not enabled$")
    public void verifyElementNotEnabled(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is not enabled: " + formattedLocator);
        Assert.assertFalse(formattedLocator + "Element is not enabled", uiBasePage.isElementEnabled(formattedLocator));

    }

    @Given("^I verify element \"([^\"]*)\" is selected$")
    public void verifyElementSelected(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is selected: " + formattedLocator);
        Assert.assertTrue(formattedLocator + "Element is selected", uiBasePage.isElementSelected(formattedLocator));

    }

    @Given("^I verify element \"([^\"]*)\" is not selected$")
    public void verifyElementNotSelected(String locator) {
        String formattedLocator = configvariable.expandValue(locator);
        logger.info("Verify element is not selected: " + formattedLocator);
        Assert.assertFalse(formattedLocator + "Element is not selected", uiBasePage.isElementSelected(formattedLocator));

    }

    @Then("I sleep for (.*) sec")
    public void waitTime(int time) throws InterruptedException {
        uiBasePage.waitTime(time);
    }

    @Then("^I get parent browser window handle into \"([^\"]*)\"$")
    public void getParentWindowHandle(String var) {
        configvariable.setStringVariable(uiBasePage.getParentWindowHandle(), var);
    }

    @Then("^I switch to parent browser window \"([^\"]*)\"$")
    public void switchToParentBrowserWindow(String windowHandle) {
        uiBasePage.switchToParentWindow(windowHandle);
    }

    @Given("^I get attribute \"([^\"]*)\" of element \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getElementAttribute(String attributeName, String locator, String variable) {
        configvariable.setStringVariable(uiBasePage.getAttributeValue(locator, attributeName), variable);

    }

    @Then("^I scroll to the start of the browser$")
    public void scrollPageUp() throws InterruptedException {
        uiBasePage.scrollWebPageUP();
    }

    @Then("^I scroll to the end of the browser$")
    public void scrollDown() throws InterruptedException {
        uiBasePage.scrollWebPageDown();
    }

    @Then("^I close current browser$")
    public void closeBrowser() {
        uiBasePage.closeBrowser();
    }

    @Then("^I close all browser$")
    public void closeAllBrowser() {
        uiBasePage.closeAllBrowser();
    }

    @Given("^I select checkbox \"([^\"]*)\"$")
    public void selectCheckbox(String locator) {
        uiBasePage.selectCheckbox(locator);
    }

    @Given("^I unselect checkbox \"([^\"]*)\"$")
    public void unSelectCheckbox(String locator) {
        uiBasePage.unSelectCheckbox(locator);
    }

    @Given("^I get browser title into variable \"([^\"]*)\"$")
    public void getBrowserTitle(String variable) {
        configvariable.setStringVariable(uiBasePage.getBrowserTitle(), variable);

    }

    @Given("^I get current url into variable \"([^\"]*)\"$")
    public void getCurrentURL(String variable) {
        configvariable.setStringVariable(uiBasePage.getCurrentURL(), variable);

    }

    @Then("^I scroll browser till element \"([^\"]*)\"$")
    public void scrollBrowserTillElement(String locator) {
        uiBasePage.scrollBrowserTillElement(locator);
    }

    @Given("^I click element \"([^\"]*)\" using java script$")
    public void clickElementJs(String locator) {
        uiBasePage.clickButtonUsingJs(locator);
    }

    @Given("^I upload file \"([^\"]*)\" on element \"([^\"]*)\"$")
    public void uploadFile(String filePath, String locator) {
        uiBasePage.uploadFileToBrowser(filePath, locator);
    }

    @Given("^I get color attribute \"([^\"]*)\" of element \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getElementColor(String attributeName, String locator, String variable) {
        configvariable.setStringVariable(uiBasePage.getElementColorAttribute(locator, attributeName), variable);
    }

    @Given("^I mouse hover and click element \"([^\"]*)\"$")
    public void mouseHoverAndClickElement(String locator) {
        uiBasePage.mouseHoverAndClickOnElement(locator);
    }

    @Given("^I mouse hover on element \"([^\"]*)\"$")
    public void mouseHoverOnElement(String locator) {
        uiBasePage.mouseHoverOnElement(locator);
    }

    @Given("^I scroll browser by X-coordinate \"([^\"]*)\" and Y-coordinate \"([^\"]*)\"$")
    public void scrollByCoordinates(String xCord, String yCord) {
        uiBasePage.scrollTillCoordinate(xCord, yCord);
    }

    @Then("^I switch to child window jandle$")
    public void switchToChildWindow() {
        uiBasePage.switchToChildWindow();
    }

    @Given("^I get alert text into variable \"([^\"]*)\"$")
    public void getAlertText(String variable) {
        configvariable.setStringVariable(uiBasePage.getAlertText(), variable);
    }

    @Then("^I accept alert$")
    public void acceptAlert() {
        uiBasePage.acceptAlert();
    }

    @Given("^I switch to frame by id \"([^\"]*)\"$")
    public void switchFrameByID(String frameId) {
        uiBasePage.switchFrameByID(frameId);
    }

    @Given("^I switch to frame by name \"([^\"]*)\"$")
    public void switchFrameByName(String frameName) {
        uiBasePage.switchFrameByName(frameName);
    }

    @Then("^I switch to default content$")
    public void switchToDefaultWindow() {
        switchToDefaultWindow();
    }

    @Given("^I get table \"([^\"]*)\" row count into variable \"([^\"]*)\"$")
    public void getTableRowCount(String locator, String variable) {
        configvariable.setStringVariable(Integer.toString(uiBasePage.getTableRowCount(locator)), variable);
    }

    @Given("^I get table \"([^\"]*)\" column count into variable \"([^\"]*)\"$")
    public void getTableColumnCount(String locator, String variable) {
        configvariable.setStringVariable(Integer.toString(uiBasePage.getTableColumnCount(locator)), variable);
    }

    @Given("^I get \"([^\"]*)\" table cell data from row \"([^\"]*)\" and column \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getTableCellData(String locator, String row, String col, String variable) {
        configvariable.setStringVariable(uiBasePage.getTableCellData(locator, row, col), variable);
    }

    @Given("^I get table \"([^\"]*)\" column index \"([^\"]*)\" into variable \"([^\"]*)\"$")
    public void getTableColumnCount(String locator, String columnName, String variable) {
        configvariable.setStringVariable(Integer.toString(uiBasePage.getTableColumnIndex(locator, columnName)), variable);
    }

    @When("^I verify following table headers are present on table \"([^\"]*)\"$")
    public void verifyTableHeader(String locator, DataTable headerList) {
        List<String> expectedHeaderList;
        expectedHeaderList = headerList.asList(String.class);
        List<String> actualHeaderList = uiBasePage.getTableHeaders(locator);
        for (int iCount = 0; iCount < expectedHeaderList.size(); iCount++) {
            Assert.assertTrue(configvariable.expandValue(expectedHeaderList.get(iCount)) + " header is present on ", actualHeaderList.contains(configvariable.expandValue(expectedHeaderList.get(iCount))));
        }
    }

    @Given("^I launch \"([^\"]*)\" mobile application$")
    public void launchMobileApplication(String platform) throws MalformedURLException {
        uiBasePage.launchMobileApplication(platform);
    }


    @Given("^I get device platform into variable \"([^\"]*)\"$")
    public void getDevicePlatform(String variable) {
        configvariable.setStringVariable(uiBasePage.getDevicePlatform(), variable);
    }

    @Given("^I swipe mobile down by duration \"([^\"]*)\"$")
    public void swipeMobileDown(String duration) {
        uiBasePage.swipeMobileDown(duration);
    }

    @Given("^I swipe mobile up by duration \"([^\"]*)\"$")
    public void swipeMobileUp(String duration) {
        uiBasePage.swipeMobileUp(duration);
    }

    @Given("^I swipe mobile down till element displayed \"([^\"]*)\"$")
    public void swipeMobileDownTillElementVisible(String locator) {
        uiBasePage.swipeMobileDownTillElementVisible(locator);
    }

    @Given("^I swipe mobile up till element displayed \"([^\"]*)\"$")
    public void swipeMobileUpTillElementVisible(String locator) {
        uiBasePage.swipeMobileUpTillElementVisible(locator);
    }

    @Given("^I set mobile capability \"([^\"]*)\" as \"([^\"]*)\"$")
    public void setMobileCapability(String capabilityKey, String capabilityVal) {
        uiBasePage.addMobileCapability(configvariable.expandValue(capabilityKey), configvariable.expandValue(capabilityVal));
    }

    @Given("^I hide mobile keyboard$")
    public void hideKeyboard() {
        uiBasePage.hideKeyboard();
    }

//    @Given("^I take screenshot$")
//    public void takeScreenshot(Scenario scenario) {
//        uiBasePage.embedScenarioScreenshot(scenario);
//    }

}
