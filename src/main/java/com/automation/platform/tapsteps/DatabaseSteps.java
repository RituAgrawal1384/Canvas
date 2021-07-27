package com.automation.platform.tapsteps;

import com.automation.platform.config.Configvariable;
import com.automation.platform.config.TapBeansLoad;
import com.automation.platform.cucumberUtils.ScenarioUtils;
import com.automation.platform.database.DatabaseMethods;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseSteps {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSteps.class);

    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private DatabaseMethods databaseMethods = (DatabaseMethods) TapBeansLoad.getBean(DatabaseMethods.class);
    private ScenarioUtils scenarioUtils = (ScenarioUtils) TapBeansLoad.getBean(ScenarioUtils.class);

    private Connection connection = null;

    @Given("^I connect to \"([^\"]*)\" database$")
    public void connectToDBType(String dbType) {
        logger.info("Setting up connection to DB " + dbType);
        connection = databaseMethods.connectToPostgresQLDB(dbType);
        logger.info("DB connection established");
        scenarioUtils.write("DB connection established");
    }

    @Given("^I execute below DB query and get value into the same variables$")
    public void executeDBQuery(String dbQuery) {
        ResultSet resultSet = databaseMethods.getResultSet(connection, configvariable.expandValue(dbQuery));
        logger.info("DB result set is ", resultSet);
        try {
            ResultSetMetaData columns = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int iCount = 1; iCount <= columns.getColumnCount(); ++iCount) {
                    String columnName = columns.getColumnName(iCount);
                    String value = resultSet.getString(columnName);
                    scenarioUtils.write("Value for column " + columnName + " is " + value);
                    configvariable.setStringVariable(value, columnName.toUpperCase());
                }
            }
        } catch (SQLException e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Please check your query to execute[{}]", e.getMessage());
        }
    }

    @Given("^I verify expected values with actual values as below$")
    public void verifyExpectedAndActualValues(DataTable valuesMap) {
        Map<String, String> valueExpectedAndActual;
        valueExpectedAndActual = valuesMap.asMap(String.class, String.class);
        for (String key : valueExpectedAndActual.keySet()) {
            String expectedValue = configvariable.expandValue(key);
            String actualValue = configvariable.expandValue(valueExpectedAndActual.get(key));
            logger.info("Expected Value is :" + expectedValue);
            logger.info("Actual value is :" + actualValue);
            scenarioUtils.write("Expected value is :" + expectedValue);
            scenarioUtils.write("Actual value is :" + actualValue);
            Assert.assertEquals(actualValue, expectedValue);
        }
    }
}
