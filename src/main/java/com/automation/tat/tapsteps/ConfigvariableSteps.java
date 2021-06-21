package com.automation.tat.tapsteps;

import com.automation.tat.cucumberUtils.ScenarioUtils;
import com.automation.tat.filehandling.FileReaderUtil;
import com.automation.tat.config.Configvariable;
import com.automation.tat.config.TapBeansLoad;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

public class ConfigvariableSteps {
    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private ScenarioUtils scenarioUtils = (ScenarioUtils) TapBeansLoad.getBean(ScenarioUtils.class);

    @And("I assign \"([^\"]*)\" to variable \"([^\"]*)\"")
    public void assignValueToVariable(String value, String Variable) {
        scenarioUtils.write("Assigning value " + configvariable.expandValue(value) + " to variable " + configvariable.expandValue(Variable));
        configvariable.assignValueToVar(value, Variable);
    }

    @When("I generate random number and assign to variable \"([^\"]*)\"")
    public void generateRandomNumberAndAssignToVariable(String varName) {
        Random rand = new Random();
        int random_num = rand.nextInt(1000);
        String num = Integer.toString(random_num);
        scenarioUtils.write("Random number generated is :" + num);
        configvariable.setStringVariable(num + configvariable.generateRandomNumber("ddMMHMs"), varName);
    }

    @When("I assign value to following variables")
    public void assignValueToVariables(DataTable userDetails) {
        Map<String, String> variableMap;
        variableMap = userDetails.asMap(String.class, String.class);
        configvariable.assignValueToVarMap(variableMap);
    }

    @When("I calculate age of the user is (.*) in \"([^\"]*)\" format from current date and assign to variable \"([^\"]*)\"")
    public void calculateAge(int age, String format, String varName) {
        configvariable.setStringVariable(configvariable.minusYearFromCurrentDate(age, format), varName);
    }

    @When("I generate time in format \"([^\"]*)\" and assign to variable \"([^\"]*)\"")
    public void generateDateAndTime(String format, String varName) {
        configvariable.setStringVariable(configvariable.generateRandomNumber(format), varName);
    }

    @When("I expect the value of var \"([^\"]*)\" equals to \"([^\"]*)\"")
    public void verifyStringMatches(String value1, String value2) {
        configvariable.match(configvariable.expandValue(value1), configvariable.expandValue(value2));
    }

    @Given("^I copy the csv template \"([^\"]*)\" and replace following variables in output path \"([^\"]*)\"$")
    public void replaceParamsInCSVFileAndGenerateOutputFile(String inputPath, String outputPath, DataTable variables) {
        Map<String, String> variableMap;
        variableMap = variables.asMap(String.class, String.class);
        configvariable.assignValueToVarMap(variableMap);
        InputStream initialStream = this.getClass().getResourceAsStream(configvariable.expandValue(inputPath));
        String inputString = FileReaderUtil.convertFileToString(initialStream);
        inputString = inputString.substring(0, inputString.length() - 1);
        String outPath = configvariable.getBaseDirectory() + "/src/test/resources" + configvariable.expandValue(outputPath);
        configvariable.setStringVariable(outPath, "OUTPUT_PATH");
        FileReaderUtil.deleteFile(outPath);
        FileReaderUtil.writeToFileUsingBufferWriter(outPath, configvariable.expandValue(inputString));
    }

    @Given("I assign the downloaded file \"([^\"]*)\" to variable \"([^\"]*)\"")
    public void assignDownloadedFilePathToVariable(String fileName, String path) {
        String fileAbsPath = configvariable.getBaseDirectory() + "\\" + configvariable.expandValue(fileName);
        configvariable.setStringVariable(fileAbsPath.replace("\\", "/"), path);
    }

    @Given("^I delete the downloaded file \"([^\"]*)\" if it already exists$")
    public void deleteDownloadedFile(String filePath) {
        FileReaderUtil.deleteFile(configvariable.expandValue(filePath));
    }

    @When("I calculate \"([^\"]*)\" from current date in \"([^\"]*)\" format and assign to variable \"([^\"]*)\"")
    public void calculateDate(String timeToUpdate, String dateFormat, String varName) {
        String date = null;
        LocalDateTime today = LocalDateTime.now();
        String firstChar = timeToUpdate.substring(0, 1);
        String LastChar = timeToUpdate.substring(timeToUpdate.length() - 1, timeToUpdate.length());
        String numberToCalculate = timeToUpdate.substring(1, timeToUpdate.length() - 1);
        int calculatedNum = Integer.parseInt(numberToCalculate);
        if (firstChar.equals("-") && LastChar.equalsIgnoreCase("d")) {
            date = configvariable.formatDateAndTime(dateFormat, today.minusDays(calculatedNum));
        } else if (firstChar.equals("+") && LastChar.equalsIgnoreCase("d")) {
            date = configvariable.formatDateAndTime(dateFormat, today.plusDays(calculatedNum));
        } else if (firstChar.equals("-") && LastChar.equalsIgnoreCase("m")) {
            date = configvariable.formatDateAndTime(dateFormat, today.minusMonths(calculatedNum));
        } else if (firstChar.equals("+") && LastChar.equalsIgnoreCase("m")) {
            date = configvariable.formatDateAndTime(dateFormat, today.plusMonths(calculatedNum));
        } else if (firstChar.equals("-") && LastChar.equalsIgnoreCase("y")) {
            date = configvariable.formatDateAndTime(dateFormat, today.minusYears(calculatedNum));
        } else if (firstChar.equals("+") && LastChar.equalsIgnoreCase("y")) {
            date = configvariable.formatDateAndTime(dateFormat, today.plusYears(calculatedNum));
        }
        configvariable.setStringVariable(date, varName);
    }

    @And("I set system property \"([^\"]*)\" to value \"([^\"]*)\"")
    public void setSystemProperty(String Variable, String value) {
        scenarioUtils.write("Assigning value " + configvariable.expandValue(value) + " to system variable " + configvariable.expandValue(Variable));
        System.setProperty(configvariable.expandValue(Variable), configvariable.expandValue(value));
    }

}
