## TAP is a platform which provides various testing automation capability as below
1. Web application using selenium
2. Mobile app using selenium and appium
3. GraphQL, Rest Assured, WebSocket api 
4. PDF automation
5. Different file reading capability
6. Database connection

## Prerequisite: Development environment should be setup
## Common
       Java
       Maven
       Intellij or other IDE
       git
## For Mobile automation
    Xcode
    Appium Desktop
    Android Studio
    Mac
    Real iOS and Android devices
    
## Setup system path variable
   1. JAVA_HOME  → Path of JDK bin directory
   2. MAVEN_HOME  → Path of maven directory
   3. ANDROID_HOME → Path of SDK
   4. Add all the above path into system Path variable

## How to use TAP steps to write test scenarios without glue code?

1. Put settings.xml file in your .m2 directory and change the credential details (You can modify the file based on your project requirement)
2. Create maven project
3. Add tap dependency as below in your pom.xml file (Need to work on release version)
4.<dependency>
    <groupId>com.automation.tap</groupId>
    <artifactId>test-automation-platform</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
5. Write feature file using below steps
6. Create runner java class file e.g:  CucumberRunner.txt . Use the attached file for reference for creating your runner class
7. Add environment variable DEVICEFARM_DEVICE_PLATFORM_NAME=android/ios in runner edit configuration of runner file(For Mobile app only)
8. If want to run browser in hidden mode add vm argument -Dweb.driver.headless=true
9. Run your test using cucumber runner file  
10. Maven command to run test using CICD(e.g. Jenkins/Bamboo): mvn clean test -Dcucumber.options="--tags @regression --tags ~@ignore" -Dapp.env=UAT -Dapp.language=en -Dapp.lbu=th -Dweb.browser.type=chrome -Dplatform.type=desktop -Dsystem.proxy.apply=true -Dweb.driver.headless=true


## Config Steps
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

PDF Steps
@When("^I verify \"([^\"]*)\" pdf file is matching with \"([^\"]*)\" pdf file and exceptions are written in \"([^\"]*)\"$")

@When("^I verify PDF file \"([^\"]*)\" should contain following values$")

@When("^I verify PDF file \"([^\"]*)\" should contain following values in page number (.*)$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain following values$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain following values in page number (.*)$")

@When("^I verify downloaded PDF file \"([^\"]*)\" should contain the values in file \"([^\"]*)\"$")



## DB Steps
@Given("^I connect to \"([^\"]*)\" database$")

@Given("^I execute below DB query and get value into the same variables$")

## Web and Mobile Steps
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


Example of Mobile:  mobiletemplate.zip

Background:
<!--     Given I load property file "/locators/th/en_android.csv" into global property map -->
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
    Then I verify element "${text.group.insurance}" is displayed
    And I click on element "${tile.make.claim}"
    And I sleep for 5 sec
    And I swipe mobile down till element displayed "${button.permanent.dissability}"
    And I click on element "${button.permanent.dissability}"
    And I click on element "${button.arrow}"
    And I sleep for 5 sec
    And I swipe mobile down by duration "6000"

Web Automation:

Scenario: Login to Web portal

  Given I launch browser application "APP_URL"
  
  When I enter text "<username>" on element "//input[@id='email']"
 
  When I enter text "<password>" on element "//input[@id='password']"
 
  And I click on element "//button[@type='submit'][text()='${login.button.text}']"
 
  Then I verify element "//h2[text()='${welcome.page}']" is displayed

## API Steps
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

Example:APITestingPOC.zip

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
 
  And I set graphql request body from file "/testdata/identity/login.graphql"

  And I send request "post" to api
 
  Then I verify response code is 200
 
  And I get response value for node "data.login.token" into variable "LOGIN_TOKEN"
 
  And I set api headers as below
    | fullstackdeployheader | blue                  |
    | content-type          | application/json      |
    | Authorization         | Bearer ${LOGIN_TOKEN} |
 
  And I set graphql request body from file "/testdata/quotations/Policy.graphql"
 
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
 
  #And I set multipart key "Users.csv" as file "/testdata/user_upload/output/Users.csv"
 
   And I send request "post" to api
 
   Then I verify response code is 200

Scenario: Get request
 
  Given I create connection for api service
 
  And I set endpoint url as "<Your endpoint>"
 
  And I send request "get" to api
 
  Then I verify response code is 200


How to create custom steps using Page object model
While writing function/regression scenarios if you come across in situation where you need to write a custom step apart from the default steps in TAP. Follow below guidelines.

 
**Template Project using page object modal**: mobiletemplate_POM.zip

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
    enterOTP("123456");
}
 
 
## How to run tests in Real Device in local Android:
 1. Run cucumberRunner.java file as testng and pass system properties as  -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dandroid.app.package=<YOUR_APP_PACKAGE> -Dandroid.app.activity=<YOUR_APP_ACTIVITY>
 2. and set environment variable DEVICEFARM_DEVICE_PLATFORM_NAME=Android 
 
 
## How to run tests in Real Device in local IOS:
  1. Run cucumberRunner.java file as testng and pass system properties as  -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dios.app.bundle.id=<APP_BUNDLE_ID>
  2. and set environment variable DEVICEFARM_DEVICE_PLATFORM_NAME=Ios 
 
## How to run in device farm Android: 
 1. Create test package using command mvn clean package -DskipTests=true
 2. Open device farm and create new run
 3. Attach you apk file 
 4. attach the zip file created in step 1
 5. Change the default test spec file in device farm as
 e.g: - java -Dappium.screenshots.dir=$DEVICEFARM_SCREENSHOT_PATH -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dandroid.app.package=my.v1rtyoz.helloworld -Dandroid.app.activity=my.v1rtyoz.helloworld.MainActivity org.testng.TestNG -testjar *-tests.jar -d $DEVICEFARM_LOG_DIR/test-output -verbose 10
 6. select devices to run
 
 ## How to run in device farm Ios: 
  1. Create test package using command mvn clean package -DskipTests=true
  2. Open device farm and create new run
  3. Attach you ipa file 
  4. attach the zip file created in step 1
  5. Change the default test spec file in device farm as
  e.g: - java -Dappium.screenshots.dir=$DEVICEFARM_SCREENSHOT_PATH -Dapp.env=UAT -Dapp.lbu=th -Dapp.language=en -Dios.app.bundle.id=<APP_BUNDLE_ID> org.testng.TestNG -testjar *-tests.jar -d $DEVICEFARM_LOG_DIR/test-output -verbose 10
  6. select devices to run
