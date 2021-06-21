//package com.automation.tap.selenium;
//
//import com.automation.tap.config.Configvariable;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//@ComponentScan(basePackages = {"com.automation.tap"})
//@Configuration
//public class SeleniumBaseTest {
//
//    public static ConfigurableApplicationContext context;
//
//
//    private SeleniumBase seleniumBase;
//    private ExecuteCommand executeCommand;
//    private Configvariable configvariable;
//
//    @BeforeClass
//    public void setup() {
//        if (context == null) {
//            context = new AnnotationConfigApplicationContext(SeleniumBaseTest.class);
//            seleniumBase = context.getBean(SeleniumBase.class);
//            executeCommand = context.getBean(ExecuteCommand.class);
//            configvariable = context.getBean(Configvariable.class);
//        }
//        configvariable.setStringVariable("false", "system.proxy.apply");
//
//    }
//
//    @Test
//    public void launchAppWithUsernameAndPass() {
//        seleniumBase.initializeSeleniumFramework();
////        seleniumBase.launchApplication("http://the-internet.herokuapp.com/basic_auth", "admin", "admin");
////        configvariable.setStringVariable("yes","web.cookie.required");
////        configvariable.setStringVariable("blueGreen","web.cookie.type");
//        configvariable.setStringVariable("blue;green", "bluegreen.cookie.keys");
//        configvariable.setStringVariable("false;true", "bluegreen.cookie.values");
//        configvariable.setStringVariable("type_domain", "bluegreen.cookie.domain");
//
//        seleniumBase.launchApplication("type_app_url", "", "");
//        if (configvariable.isCookieRequired()) {
////            seleniumBase.setBrowserCookie();
//            seleniumBase.addDriverCookie("blue;green","false;true","type_domain");
//        }
//
//        seleniumBase.refreshBrowser();
//
//
////        SeleniumBase.driver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/popup.html");
////        SeleniumBase.driver.findElement(By.xpath("//input[@placeholder='Name']")).sendKeys("test");
////        SeleniumBase.driver.findElement(By.xpath("//input[@placeholder='Value']")).sendKeys("test");
//
//        // setup ModHeader with two headers (token1 and token2)
////        ((JavascriptExecutor)SeleniumBase.driver).executeScript(
////                "localStorage.setItem('profiles', JSON.stringify([{                " +
////                        "  title: 'Selenium', hideComment: true, appendMode: '',           " +
////                        "  headers: [                                                      " +
////                        "    {enabled: true, name: 'token1', value: '01234', comment: ''}, " +
////                        "    {enabled: true, name: 'token2', value: '56789', comment: ''}  " +
////                        "  ],                                                              " +
////                        "  respHeaders: [],                                                " +
////                        "  filters: []                                                     " +
////                        "}]));                                                             " );
//
////        SeleniumBase.driver.get("type_app_url");
//        seleniumBase.quitDriver();
//    }
//
//
//    @Test
//    public void launchAppWithoutUsernameAndPass() {
//        seleniumBase.initializeSeleniumFramework();
//        seleniumBase.launchApplication("http://the-internet.herokuapp.com/basic_auth", "", "");
//        seleniumBase.quitDriver();
//    }
//
//    @Test
//    public void launchAppWithoutUsernameAndPassInIE() {
//        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
//            System.setProperty("web.browser.type", "ie");
//            seleniumBase.initializeSeleniumFramework();
//            seleniumBase.launchApplication("http://www.google.com", "", "");
//            seleniumBase.quitDriver();
//        }
//    }
//
//    @Test
//    public void launchAppWithoutUsernameAndPassInFirefox() {
//        System.setProperty("web.browser.type", "firefox");
//        seleniumBase.initializeSeleniumFramework();
//        seleniumBase.launchApplication("http://the-internet.herokuapp.com/basic_auth", "admin", "admin");
//        seleniumBase.quitDriver();
//    }
//
//    @Test
//    public void launchAppWithoutUsernameAndPassInPhantomJS() {
//        System.setProperty("web.browser.type", "phantom");
//        seleniumBase.initializeSeleniumFramework();
//        seleniumBase.launchApplication("http://the-internet.herokuapp.com/basic_auth", "admin", "admin");
//        seleniumBase.quitDriver();
//    }
//
//    @Test
//    public void launchAppWithoutUsernameAndPassInEdge() {
//        System.setProperty("web.browser.type", "edge");
//        seleniumBase.initializeSeleniumFramework();
//        seleniumBase.launchApplication("http://the-internet.herokuapp.com/", "", "");
//        seleniumBase.quitDriver();
//    }
//
//    @Test
//    public void selectDropDown() {
//        System.setProperty("web.browser.type", "chrome");
//        seleniumBase.initializeSeleniumFramework();
//        seleniumBase.launchApplication("http://the-internet.herokuapp.com", "", "");
//        executeCommand.executeCommand(SeleniumBase.driver, Commands.WAIT_FOR_ELEMENT_TO_PRESENT, null, "//a[text()=\"Dropdown\"]", "20");
//        executeCommand.executeCommand(SeleniumBase.driver, Commands.CLICK, null, "//a[text()=\"Dropdown\"]", "");
//        executeCommand.executeCommand(SeleniumBase.driver, Commands.SELECT_BY_VISIBLE_TEXT, null, "//select[@id=\"dropdown\"]", "Option 2");
//        executeCommand.executeCommand(SeleniumBase.driver, Commands.GET_SELECTED_TEXT, null, "//select[@id=\"dropdown\"]", "");
//        String text = configvariable.getStringVar("selected.dropdown.value");
//        Assert.assertTrue(!text.isEmpty());
//        seleniumBase.quitDriver();
//    }
//
//}
