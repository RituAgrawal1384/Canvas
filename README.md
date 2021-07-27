# Getting started with CANVAS
Description: Canvas is a suite of tools that provides a single platform to write Test Automation scripts in cucumber BDD approach for different types of application(Web, Mobile), API, Databases, PDF comparison etc.
             
    This has been developed keeping in mind that testers do not have to invest time in setting up the framework and can straight forward start writing their test scripts with minimal knowledge of automation/programming language.
    The CANVAS architecture is also quite simple and easy for new users to understand and start using it of their own.

## How Canvas is different from other open source test automation framework/tool
    1. Configuration is simple,minimal and easy to use as mentioned below. User can setup the repo using template project within few hours and start testing.
    2. No extra layer of complexity for writing test scripts. You can directly use the class from CANVAS to write your own scripts
    3. Template projects are provided to easy start your automation 
    4. All the driver waits are handled internally and dynamically and hence less chances of script failure incase user does not speficy that
    5. Supports writing of Unit/Integration/System/End to End/Acceptance tests using Cucumber BDD / testng
    6. It does not require extensive training or document reference for writing scripts
    7. There is no need to maintain driver version to support the browsers. Canvas will automatically download the required browser driver for different platform e.g: max/windows/linux
    8. There are other features provided by Canvas which can be witnessed while using CANVAS

# HOME

[CANVAS Capabilities](#canvas-capabilities)

[Development environment setup](#development-environment-setup)

[How to use CANVAS steps to write test scenarios without glue code](#how-to-use-canvas-steps-to-write-test-scenarios-without-glue-code)

[Config Steps](#config-steps)

[PDF Steps](#pdf-steps)

[Excel steps](#excel-steps)

[Azure File Share Steps](#azure-fileshare-steps)

[DB Steps](#db-steps)

[Web and Mobile Steps](#web-and-mobile-steps)

[API Steps](#api-steps)

[How to create custom steps using Page object model](#how-to-create-custom-steps-using-page-object-model)

[Passing data/variables between cucumber scenarios/steps](#passing-data-between-cucumber-scenarios)

[Adding IOS/Android desired capabilities and Browser option](#adding-desired-capability-to-appium-or-web-driver)

[Downloading specific version of driver using WebDriverManager](#download-specific-browser-driver)

[How to run tests in Real Device in local Android](#how-to-run-tests-in-real-device-in-local-android)

[How to run tests in Real Device in local IOS](#how-to-run-tests-in-real-device-in-local-ios)

[How to run in device farm Android](#how-to-run-in-aws-device-farm-android)

[How to run in device farm Ios](#how-to-run-in-aws-device-farm-ios)

[Upcoming features](#upcoming-features)


# CANVAS Capabilities
1. Web application automation using selenium
2. Mobile app automation using selenium and appium
3. GraphQL, Rest API, WebSocket API automation
4. PDF automation
5. Different file reading capability e.g: csv, excel, xml, Json, text, azure file storage etc
6. Database connection
7. Reports in html and pdf format
8. Support to run Mobile app in AWS device farm
9. Support to run web app through CICD pipelines
10. Throw custom exceptions in your project using TAPException class
11. Global properties/variables map which can be accessed anywhere in the entire test suite during execution. Refer attached example projects for this reference 
12. Inbuilt cucumber BDD steps to support Web/Mobile/API automation to ease the automation writing. Example projects attached are created using inbuilt steps for reference
13. Canvas classes can be accessed by either using sprint annotation or creating the required class object. Example projects attached are created using sprint annotation for reference
14. Project can be configured to take screen-shot on failure or when ever required using Canvas provided method

[Back-To-Home](#home)
# Development environment setup
### Common
       Java (JDK-11)
       Maven
       Intellij or other IDE
       git
### For Mobile automation
        Xcode
        Appium Desktop
        Android Studio
        Mac
        Real iOS and Android devices
    
### Setup system path variable
   1. JAVA_HOME  → Path of JDK bin directory
   2. MAVEN_HOME  → Path of maven directory
   3. ANDROID_HOME → Path of SDK
   4. Add all the above path into system Path variable

[Back-To-Home](#home)
# How to use CANVAS steps to write test scenarios without glue code
1. Create maven project
2. Add Canvas dependency as below in your pom.xml file 

                <dependency>
                    <groupId>io.github.rituagrawal1384</groupId>
                    <artifactId>test-automation-canvas</artifactId>
                    <version>1.0.2</version>
                </dependency>

3. Write feature file using inbuilt steps
4. Create runner java class file as below
   
              
              @ComponentScan(basePackages = {"com.mobile.template", "com.automation.platform"})
              @Configuration
              @CucumberOptions(
                      monochrome = true,
                      features = "classpath:features",
                      glue = {"com/mobile/template/stepdef", "com/automation/platform/tapsteps"},
                      tags = {"@test_tat", "~@ignore"},

                      plugin = {"pretty",
                              "html:reports/cucumber/cucumber-html",
                              "json:reports/cucumber/cucumber.json",
                              "usage:reports/cucumber-usage.json",
                              "junit:reports/junit/cucumber-junit.xml"}
              )

              public class CucumberRunner extends AbstractTestNGCucumberTests {

                  private static final Logger LOGGER = LoggerFactory.getLogger(CucumberRunner.class);

                  private Configvariable configvariable;
                  private SeleniumBase seleniumBase;
                  private HelperMethods helperMethods;

                  @BeforeSuite(alwaysRun = true)
                  public void setUpEnvironmentToTest() {
                      // write if anything needs to be set up once before tests run. e.g. connection to database
                      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                      TapBeansLoad.setConfigClass(CucumberRunner.class);
                      TapBeansLoad.init();
                      configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
                      seleniumBase = (SeleniumBase) TapBeansLoad.getBean(SeleniumBase.class);
                      seleniumBase.initializeSeleniumFramework();  
              
                  }

                  @AfterSuite(alwaysRun = true)
                  public void cleanUp() {
                      // close if something enabled in @before suite. e.g. closing connection to DB, driver
                      LOGGER.info("Copying and generating reports....");
                      String deviceFarmLogDir = System.getenv("DEVICEFARM_LOG_DIR");
                      TapReporting.generateReportForJsonFiles(deviceFarmLogDir);
                      
                      LOGGER.info("Quiting driver if needed....");
                      if (SeleniumBase.driver != null) {
                          SeleniumBase.driver.quit();
                      }

                      FileReaderUtil.deleteFile("reports/mobile-app-test-results.pdf");
                      TapReporting.detailedReport("reports/cucumber/cucumber.json", "mobile-app");

                  }
                }
5. Add VM arguments  -DosType=android/ios in runner edit configuration of runner file(For Mobile app only)
6. Add the app package/activity or bundle id (android.app.package=<>,android.app.activity=<>,ios.app.bundle.id=<> in your property file to work on a particular app (For mobile automation only).
7. To run browser in a hidden mode add vm argument -Dweb.driver.headless=true
8. To open Chrome browser in incognito mode add web.browser.incognito=true in your env property file
9. To bypass network proxy add vm argument -Dsystem.proxy.apply=true or in env property file
10. To change driver implicit wait add "web.implicit.wait=<time in second>" property in property file and load into global map   
11. By default, browser will open in chrome if want to execute scripts in another browser add vm argument -Dweb.browser.type=safari/firefox/ie/edge/phantom etc    
12. Run your test using cucumber runner file  
13. Maven commands to run test using CICD(e.g. Jenkins/Bamboo): mvn clean test -Dcucumber.options="--tags @regression --tags ~@ignore" -Dapp.env=UAT -Dapp.language=en -Dapp.lbu=th -Dweb.browser.type=chrome -Dplatform.type=desktop -Dsystem.proxy.apply=true -Dweb.driver.headless=true
14. The template mobile project attached below is AWS device farm execution compatible for your reference

[Back-To-Home](#home)
# Config Steps
@Given("^I verify expected values with actual values as below$")

@And("I assign \"([^\"]*)\" to variable \"([^\"]*)\"")

@When("I generate random number and assign to variable \"([^\"]*)\"")

@When("I assign value to following variables")

@When("I calculate age of the user is (.*) in \"([^\"]*)\" format from current date and assign to variable \"([^\"]*)\"")

@When("I generate time in format \"([^\"]*)\" and assign to variable \"([^\"]*)\"")

@When("I expect the value of var \"([^\"]*)\" equals to \"([^\"]*)\"")

@Given("^I copy the csv template \"([^\"]*)\" and replace following variables in output path \"([^\"]*)\"$")

@Given("I assign the downloaded file \"([^\"]*)\" to variable \"([^\"]*)\"")

@Given("^I delete the downloaded file \"([^\"]*)\" if it already exists$")

@When("I calculate \"([^\"]*)\" from current date in \"([^\"]*)\" format and assign to variable \"([^\"]*)\"")

@And("I set system property \"([^\"]*)\" to value \"([^\"]*)\"")

[Back-To-Home](#home)
# PDF Steps
@When("^I verify \"([^\"]*)\" pdf file is matching with \"([^\"]*)\" pdf file and exceptions are written in \"([^\"]*)\"$")

@When("^I verify PDF file \"([^\"]*)\" should contain following values$")

@When("^I verify PDF file \"([^\"]*)\" should contain following values in page number (.*)$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain following values$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain following values in page number (.*)$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain the values in file \"([^\"]*)\"$")

[Back-To-Home](#home)
# Excel Steps
    @When I copy the xls template "${INPUT_PATH}" and replace following variables in output path "${OUTPUT_PATH}" for column "Company Email"
    | email.id | ${COMP_EMAIL_ID} |

    @When I write to excel file "${EXCEL_FILE_PATH}" into below rows for column "Exported Fields" and search column name "Value Fields"
    | Status | APPROVED         |
    | Number | ${RANDOM_NUMBER} |

    @And I verify excel file "${EXCEL_FILE_PATH}" contains following values for column "Exported Fields" for search columns "Value Fields"
    | Company Name     | ${COMP_NAME}          |
    | Ref Number       | ${REF_NUMBER}         |
    | Total            | 10                    |

    @When I write to excel file "${EXCEL_FILE_PATH}" into below file rows for column "Employee Number" and search column name "Company Email"
    """
    /testdata/excelcolumn/Key_value_map.csv
    """

    @When I verify excel file "${EXCEL_FILE_PATH}" is matching with "${to.path}" for column "Value Fields"

[Back-To-Home](#home)
# Azure FileShare Steps
@When I download azure storage file "${AZURE_FILE_NAME}" from storage folder "${AZURE_SRC_FOLDER_NAME}" to location "${DOWNLOADED_FILE_PATH}"

@And I upload local file "${AZURE_FILE_NAME}" to storage folder "${AZURE_DEST_FOLDER_NAME}" from location "${DOWNLOADED_FILE_PATH}"

@Then I wait for storage file "${AZURE_FILE_NAME}" to be processed from storage folder "${AZURE_DEST_FOLDER_NAME}"

@Then("^I get azure storage file list from storage folder \"([^\"]*)\" for request \"([^\"]*)\" with in timestamp1 \"([^\"]*)\" and timestamp2 \"([^\"]*)\"$")


[Back-To-Home](#home)
# DB Steps
@Given("^I connect to \"([^\"]*)\" database$")

@Given("^I execute below DB query and get value into the same variables$")

[Back-To-Home](#home)
# Web and Mobile Steps
@Given("^I set browser type as \"([^\"]*)\"$")

@Given("^I open browser in hidden mode \"([^\"]*)\"$")

@Given("^I set default download directory as \"([^\"]*)\"$")

@Given("^Initialize selenium framework$")

@Given("^I launch browser application \"([^\"]*)\"$")

@Given("^I launch browser application \"([^\"]*)\" with browser authentication username \"([^\"]*)\" and \"([^\"]*)\" password$")

@Given("^I set browser cookie name \"([^\"]*)\" and value \"([^\"]*)\" for domain \"([^\"]*)\"$")

@Given("^I delete browser cookie name \"([^\"]*)\"$")

@Given("^I navigate to url \"([^\"]*)\"$")

@Given("^I refresh browser$")

@Given("^I click on element \"([^\"]*)\"$")

@Given("^I clear text on element \"([^\"]*)\"$")

@Given("^I enter text \"([^\"]*)\" on element \"([^\"]*)\"$")

@Given("^I enter text \"([^\"]*)\" on element \"([^\"]*)\" with tab$")

@Given("^I get text of element \"([^\"]*)\" into variable \"([^\"]*)\"$")

@Given("^I verify element \"([^\"]*)\" is displayed$")

@Given("^I verify element \"([^\"]*)\" is not displayed$")

@Given("^I verify element \"([^\"]*)\" is enabled$")

@Given("^I verify element \"([^\"]*)\" is not enabled$")

@Given("^I verify element \"([^\"]*)\" is selected$")

@Given("^I verify element \"([^\"]*)\" is not selected$")

@Then("I sleep for (.*) sec")

@Then("^I get parent browser window handle into \"([^\"]*)\"$")

@Then("^I switch to parent browser window \"([^\"]*)\"$")

@Given("^I get attribute \"([^\"]*)\" of element \"([^\"]*)\" into variable \"([^\"]*)\"$")

@Then("^I scroll to the start of the browser$")

@Then("^I scroll to the end of the browser$")

@Then("^I close current browser$")

@Then("^I close all browser$")

@Given("^I select checkbox \"([^\"]*)\"$")

@Given("^I unselect checkbox \"([^\"]*)\"$")

@Given("^I get browser title into variable \"([^\"]*)\"$")

@Given("^I get current url into variable \"([^\"]*)\"$")

@Then("^I scroll browser till element \"([^\"]*)\"$")

@Given("^I click element \"([^\"]*)\" using java script$")

@Given("^I upload file \"([^\"]*)\" on element \"([^\"]*)\"$")

@Given("^I get color attribute \"([^\"]*)\" of element \"([^\"]*)\" into variable \"([^\"]*)\"$")

@Given("^I mouse hover and click element \"([^\"]*)\"$")

@Given("^I mouse hover on element \"([^\"]*)\"$")

@Given("^I scroll browser by X-coordinate \"([^\"]*)\" and Y-coordinate \"([^\"]*)\"$")

@Then("^I switch to child window jandle$")

@Given("^I get alert text into variable \"([^\"]*)\"$")

@Then("^I accept alert$")

@Given("^I switch to frame by id \"([^\"]*)\"$")

@Given("^I switch to frame by name \"([^\"]*)\"$")

@Then("^I switch to default content$")

@Given("^I get table \"([^\"]*)\" row count into variable \"([^\"]*)\"$")

@Given("^I get table \"([^\"]*)\" column count into variable \"([^\"]*)\"$")

@Given("^I get \"([^\"]*)\" table cell data from row \"([^\"]*)\" and column \"([^\"]*)\" into variable \"([^\"]*)\"$")

@Given("^I get table \"([^\"]*)\" column index \"([^\"]*)\" into variable \"([^\"]*)\"$")

@When("^I verify following table headers are present on table \"([^\"]*)\"$")

@Given("^I launch \"([^\"]*)\" mobile application$")

@Given("^I get device platform into variable \"([^\"]*)\"$")

@Given("^I swipe mobile down by duration \"([^\"]*)\"$")

@Given("^I swipe mobile up by duration \"([^\"]*)\"$")

@Given("^I swipe mobile down till element displayed \"([^\"]*)\"$")

@Given("^I swipe mobile up till element displayed \"([^\"]*)\"$")

@Given("^I set mobile capability \"([^\"]*)\" as \"([^\"]*)\"$")

@Given("^I hide mobile keyboard$")

[Mobile/Web Template Project Download](https://github.com/RituAgrawal1384/tat-mobile-template-project)

**Example of Mobile:**
    
    Background:
        And I load environment property file "uat" into global property map for lbu "th"
        And I load csv file "/locators/th/en_android.csv" with separator "=" into global property map

    Scenario: Login to Mobile app
        Given I get device platform into variable "DEVICE_PLATFORM"
        When I launch "${DEVICE_PLATFORM}" mobile application
        And I enter text "${login.id.global}" on element "${text.field.email}"
        And I clear text on element "${text.field.email}"
        And I enter text "${login.id.global}" on element "${text.field.email}"
        And I swipe mobile down by duration "6000"
        And I swipe mobile down till element displayed "${button.submit}"
        And I click on element "${button.submit}"
        Then I verify element "${text.home.page}" is displayed
    

**Web Automation:**

    Scenario: Login to Web portal
        Given I launch browser application "APP_URL"
        When I enter text "<username>" on element "//input[@id='email']"
        When I enter text "<password>" on element "//input[@id='password']"
        And I click on element "//button[@type='submit'][text()='${login.button.text}']"
        Then I verify element "//h2[text()='${welcome.page}']" is displayed

[Back-To-Home](#home)
# API Steps
@When("I create connection for api service")

@Given("^I set endpoint url as \"([^\"]*)\"$")

@Given("^I set api header key \"([^\"]*)\" and value \"([^\"]*)\"$")

@When("^I set api headers as below$")

@Given("^I set multipart key \"([^\"]*)\" as file \"([^\"]*)\"$")

@Given("^I set multipart key \"([^\"]*)\" as text \"([^\"]*)\"$")

@When("^I set request body as below$")

@When("^I set request body from file \"([^\"]*)\"$")

@When("^I set graphql request body from file \"([^\"]*)\"$")

@When("^I verify response code is (.*)$")

@When("^I verify response value for node \"([^\"]*)\" is \"([^\"]*)\"$")

@When("^I verify response count for node \"([^\"]*)\" is (.*)$")

@When("^I get response value for node \"([^\"]*)\" into variable \"([^\"]*)\"$")

@When("I close connection for api service")

@When("^I send request \"([^\"]*)\" to api$")

@When("^I set multipart data as below$")

Example: 
[API Template Project Download](https://github.com/RituAgrawal1384/tat-api-template-project)

**Graphql**
 
    Scenario: Verify user can add employee post login to web api endpoint
        Given I generate random number and assign to variable "RAND_NUM"
        And I assign value to following variables
        | USER_NAME       | <admin User>                     |
        | PASSWORD        | <Admin Password>                 |
        | EMP_GIVEN_NAME  | Test_${RAND_NUM}                    |
        | EMP_FAMILY_NAME | Emp_${RAND_NUM}                     |
        | EMP_EMAIL       | TestEmp_${RAND_NUM}@mailinator.com  |
        | NODE            | login                               |
        Given I create connection for api service
        And I set endpoint url as "<end point>"
        And I set api headers as below
        | fullstackdeployheader | blue             |
        | content-type          | application/json |
        And I set graphql request body from file "/testdata/login/login.graphql"
        And I send request "post" to api
        Then I verify response code is 200
        And I get response value for node "data.login.token" into variable "LOGIN_TOKEN"
        And I set api headers as below
        | fullstackdeployheader | blue                  |
        | content-type          | application/json      |
        | Authorization         | Bearer ${LOGIN_TOKEN} |
        And I set graphql request body from file "/testdata/employee/addEmployee.graphql"
        And I send request "post" to api
        Then I verify response code is 200
        And I get response value for node "data.addEmployee.id" into variable "EMP_ID"
        Then I verify response value for node "data.addEmployee.givenName" is "${EMP_GIVEN_NAME}"
     

**Multipart form data**
 
    Scenario: Verify User can post multipart request
        Given I create connection for api service
        And I set endpoint url as "<Your endpoint>"
        And I set multipart data as below
        | client_secret | <secret id>     |
        | client_id     | <client id>     |
        | contact       | <PhoneNumber>   |
        | email         | <EmailAddress>  |
        And I set multipart key "Register.csv" as file "/testdata/register/output/Register.csv"
        And I send request "post" to api
        Then I verify response code is 200

    Scenario: Get request
        Given I create connection for api service
        And I set endpoint url as "<Your endpoint>"
        And I send request "get" to api
        Then I verify response code is 200

[Back-To-Home](#home)
# How to create custom steps using Page object model
While writing function/regression scenarios if you come across in situation where you need to write a custom step apart from the default steps in Canvas. 
Follow below guidelines.

 
**Template Project using page object modal**: 
[Mobile Template Project Download](https://github.com/RituAgrawal1384/tat-mobile-template-project)

Example: Login scenario. Instead of writing the same steps like enter userid, password, otp and click on login button in all feature file you can combine these steps and create your own login scenario

Feature file:

    Scenario: Example of how to write custom steps
        Given I get device platform into variable "DEVICE_PLATFORM"
        When I launch "${DEVICE_PLATFORM}" mobile application
        And I login to mobile app using email "${login.id.global}".    --> This step require in all my feature file(enter userid, click on submit button and enter otp)

**LoginStep:**
        
    @Given("^I login to mobile app using email \"([^\"]*)\"$")
    public void loginToApp(String emailId) {
        loginPage.loginToApp(emailId);
    }

LoginPage:
 
    public void loginToApp(String userName) {
        enterEmailId(userName);
        clickSubmitButton();
    }

[Back-To-Home](#home)
# Passing data between cucumber scenarios
  CANVAS provides its customized simple way to pass the data between scenarios / steps in cucumber feature file  
  Lets see this in example: In the below example "LOGIN_TOKEN" is the variable created in first scenario which contains the token value which is required to access other endpoint in rest of the scenarios

     Scenario: Get token from login api
        Given I generate random number and assign to variable "RAND_NUM"
        And I assign value to following variables
        | USER_NAME       | <admin User>                     |
        | PASSWORD        | <Admin Password>                 |
        | NODE            | login                            |
        Given I create connection for api service
        And I set endpoint url as "<end point>"
        And I set api headers as below
        | content-type          | application/json |
        And I set graphql request body from file "/testdata/login/login.graphql"
        And I send request "post" to api
        Then I verify response code is 200
        And I get response value for node "data.login.token" into variable "LOGIN_TOKEN"

     Scenario: Use the token from above scenario "LOGIN_TOKEN" in this scenario
        And I set api header key "Authorization" and value "Bearer ${LOGIN_TOKEN}"
        And I set graphql request body from file "/testdata/employee/addEmployee.graphql"
        And I send request "post" to api
        Then I verify response code is 200
        And I get response value for node "data.addEmployee.id" into variable "EMP_ID"

here  I get response value for node "data.login.token" into variable "LOGIN_TOKEN" step is using "setStringVariable()" method to set the value to the variable LOGIN_TOKEN as below
       
        @When("^I get response value for node \"([^\"]*)\" into variable \"([^\"]*)\"$")
        public void getResponseNodeData(String jsonPath, String variable) {
            String jsonP = configvariable.expandValue(jsonPath);
            String responseVal = httpClientApi.getJsonPathStringValue(jsonP);
            configvariable.setStringVariable(responseVal, variable);
        }

And  I set api header key "Authorization" and value "Bearer ${LOGIN_TOKEN}" step is using the variable as ${LOGIN_TOKEN} using the expandValue method as below
        
        @Given("^I set api header key \"([^\"]*)\" and value \"([^\"]*)\"$")
        public void setHeaders(String key, String value) {
            String headerKey = configvariable.expandValue(key);
            String headerValue = configvariable.expandValue(value);
            httpClientApi.setSendHeaders(headerKey, headerValue);
        }

[Back-To-Home](#home)
# Adding Desired Capability to Appium or Web driver
CANVAS provides user an option to add the required capability for the driver before invoking the driver
    
    IOS/Android: private TapDriver tapDriver = new TapDriver();
                tapDriver.capabilities.setCapability("newCommandTimeout", 10000);

    Web App: private TapDriver tapDriver = new TapDriver();
            tapDriver.chromeOptions.addArguments("--incognito");

[Back-To-Home](#home)
# Download specific browser driver
 WebDriverManager library provides the feature to download a specific version of driver for required browser by setting the system properties as vm argument
 Refer github page https://github.com/bonigarcia/webdrivermanager for more details    
     
     -Dwdm.chromeDriverVersion=81.0.4044.138
     -Dwdm.geckoDriverVersion=0.26.0
     -Dwdm.edgeDriverVersion=81.0.410.0
     -Dwdm.operaDriverVersion=81.0.4044.113 

[Back-To-Home](#home)
# How to run tests in Real Device in local Android
  Run cucumberRunner.java file as testng and pass system properties as  -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dandroid.app.package=<YOUR_APP_PACKAGE> -Dandroid.app.activity=<YOUR_APP_ACTIVITY> -DosType=android

[Back-To-Home](#home)
# How to run tests in Real Device in local IOS
  Run cucumberRunner.java file as testng and pass system properties as  -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dios.app.bundle.id=<APP_BUNDLE_ID> -DosType=ios

[Back-To-Home](#home)
# How to run in AWS device farm Android
 1. Create test package using command **mvn clean package -DskipTests=true**
 2. Open device farm and create new run
 3. Attach you apk file 
 4. attach the zip file created in step 1
 5. Change the default test spec file in device farm as
 e.g: - java -Dappium.screenshots.dir=$DEVICEFARM_SCREENSHOT_PATH -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dandroid.app.package=my.v1rtyoz.helloworld -Dandroid.app.activity=my.v1rtyoz.helloworld.MainActivity org.testng.TestNG -testjar *-tests.jar -d $DEVICEFARM_LOG_DIR/test-output -verbose 10
 6. select devices to run

 [Back-To-Home](#home)
 # How to run in AWS device farm Ios
  1. Create test package using command **mvn clean package -DskipTests=true**
  2. Open device farm and create new run
  3. Attach you ipa file 
  4. attach the zip file created in step 1
  5. Change the default test spec file in device farm as
  e.g: - java -Dappium.screenshots.dir=$DEVICEFARM_SCREENSHOT_PATH -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dios.app.bundle.id=<APP_BUNDLE_ID> org.testng.TestNG -testjar *-tests.jar -d $DEVICEFARM_LOG_DIR/test-output -verbose 10
  6. select devices to run

 # Upcoming features
  1. Web service Automation support
  2. Docker setup and run scripts using Web Driver in docker container
