package com.prudential.tap.filehandling;

import java.io.InputStream;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CsvUtilsTest {
    private CsvUtils csvUtils = new CsvUtils();

    @Test
    public void testReadAllCSVDataBySeparatorSemiColon() {
        String filePath = "testFile/GTL.csv";
        List<String[]> csvData = csvUtils.readAlldataFromCSVFile(filePath, ';');
        Assert.assertNotNull(csvData);
    }

    @Test
    public void testReadAllCSVDataBySeparatorByAt() {
        String filePath = "testFile/GTL_AT.csv";
        List<String[]> csvData = csvUtils.readAlldataFromCSVFile(filePath, '@');
        Assert.assertNotNull(csvData);
    }

    @Test
    public void testReadCSVHeaders() {
        String filePath = "testFile/GTL.csv";
        String[] headers = csvUtils.readCSVFileHeaders(filePath);
        Assert.assertNotNull(headers);
    }

    @Test
    public void testReadAllCSVDataByInputSteam() {
        String filePath = "/testFile/GTL.csv";
        InputStream initialStream = getClass().getResourceAsStream(filePath);
        List<String[]> csvData = csvUtils.csvInputStreamReader(initialStream, ';');
        Assert.assertNotNull(csvData);
    }
}
