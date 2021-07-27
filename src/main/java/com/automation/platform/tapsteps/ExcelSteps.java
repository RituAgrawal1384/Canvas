package com.automation.platform.tapsteps;


import com.automation.platform.config.Configvariable;
import com.automation.platform.config.TapBeansLoad;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.automation.platform.filehandling.CsvUtils;
import com.automation.platform.filehandling.ExcelUtils;
import com.automation.platform.filehandling.FileReaderUtil;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelSteps {

    private ExcelUtils excelUtils = (ExcelUtils) TapBeansLoad.getBean(ExcelUtils.class);
    private Configvariable configvariable = (Configvariable) TapBeansLoad.getBean(Configvariable.class);
    private CsvUtils csvUtils = (CsvUtils) TapBeansLoad.getBean(CsvUtils.class);

    @Then("^I write to excel file \"([^\"]*)\" into below columns for row containing value \"([^\"]*)\"$")
    public void writeToColumnForGivenRow(String filePath, String rowToSearchVal, DataTable rowData) {
        Map<String, String> rowDataMap;
        rowDataMap = rowData.asMap(String.class, String.class);
        excelUtils.writeToColumnsForGivenRow(configvariable.expandValue(filePath), 0, configvariable.expandValue(rowToSearchVal), rowDataMap);
    }

    @Then("^I write to excel file \"([^\"]*)\" into below rows for column \"([^\"]*)\" and search column name \"([^\"]*)\"$")
    public void writeToGivenRowsForGivenColumn(String filePath, String columnName, String columnNameToSearch, DataTable rowData) {
        Map<String, String> rowDataMap;
        rowDataMap = rowData.asMap(String.class, String.class);
        excelUtils.writeToRowsForGivenColumn(configvariable.expandValue(filePath), 0, configvariable.expandValue(columnName), columnNameToSearch, rowDataMap);
    }

    @Given("^I copy the xls template \"([^\"]*)\" and replace following variables in output path \"([^\"]*)\" for column \"([^\"]*)\"$")
    public void replaceParamsInXLSFileAndGenerateOutputFile(String inputPath, String outputPath, String columnName, DataTable variables) {
        Map<String, String> variableMap = variables.asMap(String.class, String.class);
        this.configvariable.assignValueToVarMap(variableMap);
        String inPath = this.configvariable.getBaseDirectory() + this.configvariable.expandValue(inputPath);
        String[] fileParts = inPath.split("/");
        String outPath = this.configvariable.getBaseDirectory() + this.configvariable.expandValue(outputPath);
        String outFile = "/" + fileParts[fileParts.length - 1];
        FileReaderUtil.deleteFile(outPath + outFile);
        File srcDir = new File(inPath);
        File desDir = new File(outPath);
        try {
            FileUtils.copyFileToDirectory(srcDir, desDir);
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Not able to copy file from directory [{}] to directory [{}]", inPath, outPath);
        }
        excelUtils.writeToAllRowsForGivenColumn(outPath + outFile, 0, configvariable.expandValue(columnName));
    }

    @Then("^I verify excel file \"([^\"]*)\" is matching with \"([^\"]*)\" for column \"([^\"]*)\"$")
    public void verifyExcelFileMatchingForFirstColumn(String fileFromPath, String fileToPath, String columnName) {
        Map<String, Map<String, String>> fromData = excelUtils.readExcelDataIntoMap(configvariable.expandValue(fileFromPath), 0, columnName);
        Map<String, Map<String, String>> toData = excelUtils.readExcelDataIntoMap(configvariable.expandValue(fileToPath), 0, columnName);
        Assert.assertEquals(fromData.keySet(), toData.keySet(), "Both Excel data matching for first row");
    }

    @Then("^I verify excel file \"([^\"]*)\" contains following values for column \"([^\"]*)\" for search columns \"([^\"]*)\"$")
    public void verifyExcelFieldData(String fileFromPath, String columnName, String searchColumn, DataTable rowData) {
        Map<String, String> dataMap = rowData.asMap(String.class, String.class);
        Map<String, Map<String, String>> fromData = excelUtils.readExcelDataIntoMap(configvariable.expandValue(fileFromPath), 0, searchColumn);
        SoftAssert softAssert = new SoftAssert();
        for (String key : dataMap.keySet()) {
            softAssert.assertEquals(fromData.get(key).get(configvariable.expandValue(columnName)).toLowerCase(), configvariable.expandValue(dataMap.get(key)).toLowerCase(), key + " = " + configvariable.expandValue(dataMap.get(key)) + " in excel file");
        }
        softAssert.assertAll();
    }

    @Then("^I write to excel file \"([^\"]*)\" into below file rows for column \"([^\"]*)\" and search column name \"([^\"]*)\"$")
    public void writeToGivenRowsInFileForGivenColumn(String filePath, String columnName, String columnNameToSearch, String rowDataCsvFile) {
        Map<String, String> rowDataMap;
        rowDataMap = loadCSVDataIntoMap(configvariable.expandValue(rowDataCsvFile), ',');
        excelUtils.writeToRowsForGivenColumn(configvariable.expandValue(filePath), 0, configvariable.expandValue(columnName), columnNameToSearch, rowDataMap);
    }

    public Map<String, String> loadCSVDataIntoMap(String fileName, char separator) {
        Map<String, String> rowData = new HashMap<>();
        InputStream initialStream = getClass().getResourceAsStream(fileName);
        List<String[]> allCsvData = csvUtils.csvInputStreamReader(initialStream, separator);
        // print Data
        for (String[] row : allCsvData) {
            rowData.put(configvariable.expandValue(row[0]), configvariable.expandValue(row[1]));
        }
        return rowData;
    }

}



