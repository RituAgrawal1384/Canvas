package com.prudential.tap.tapsteps;

import com.prudential.tap.config.Configvariable;
import com.prudential.tap.config.TapBeansLoad;
import com.prudential.tap.cucumberUtils.ScenarioUtils;
import com.prudential.tap.exception.TapException;
import com.prudential.tap.exception.TapExceptionType;
import com.prudential.tap.filehandling.CsvUtils;
import cucumber.api.java.en.Given;

import java.io.InputStream;
import java.util.List;

public class FileHandlingSteps {
    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private CsvUtils csvUtils = (CsvUtils) TapBeansLoad.getBean(CsvUtils.class);
    private ScenarioUtils scenarioUtils = (ScenarioUtils) TapBeansLoad.getBean(ScenarioUtils.class);

    @Given("^I load property file \"([^\"]*)\" into global property map$")
    public void loadPropertyFile(String filePath) {
        scenarioUtils.write("Loading property file :" + filePath);
        InputStream initialStream = getClass().getResourceAsStream(filePath);
        configvariable.propertiesFileLoad(initialStream);
    }

    @Given("^I load environment property file \"([^\"]*)\" into global property map for lbu \"([^\"]*)\"$")
    public void loadEnvironmentPropertyFile(String env, String lbu) {
        scenarioUtils.write("Loading property file for environment :" + env);
        configvariable.setupEnvironmentProperties(env, lbu);
    }

    @Given("^I load csv file \"([^\"]*)\" with separator \"([^\"]*)\" into global property map$")
    public void loadCsvFile(String filePath, String separator) {
        scenarioUtils.write("Loading csv file :" + filePath);
        InputStream initialStream = getClass().getResourceAsStream(filePath);
        List<String[]> allCsvData = csvUtils.csvInputStreamReader(initialStream, separator.charAt(0));
        try {
            for (String[] row : allCsvData) {
                configvariable.setStringVariable(row[1], row[0]);
            }
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Remove the blank lines from csv file");
        }
    }
}
