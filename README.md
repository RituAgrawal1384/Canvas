## TAP is a platform which provides various testing automation capability as below
1. Web application using selenium
2. Mobile app using selenium and appium
3. GraphQL, Rest Assured, WebSocket api 
4. PDF automation
5. Different file reading capability
6. Database connection

## How to use TAP steps:
1. Create maven project
2. Add tap dependency as below in your pom.xml file (Need to work on release version)

##dependency
##<groupId>com.prudential.tap</groupId>
##<artifactId>test-automation-platform</artifactId>
##<version>1.0-SNAPSHOT</version>
##/dependency

3. Write feature file using below steps
4. Create runner file
5. Run your test using cucumber runner file 

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


## PDF Steps
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