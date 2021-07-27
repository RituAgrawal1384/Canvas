package com.automation.platform.reporting;

import com.automation.platform.filehandling.FileReaderUtil;
import java.io.IOException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TapReportingTest {

    TapReporting tapReporting = new TapReporting();

    @BeforeTest
    public void setUp() {
        FileReaderUtil.createDirectory(System.getProperty("user.dir") + "/reports/cucumber/");
        FileReaderUtil.copyFilesToDirectory("target/test-classes/testFile", System.getProperty("user.dir") + "/reports/cucumber/");
    }

    @Test
    public void testgenerateReportForJsonFilesInLocal() throws IOException {
        String filePath = null;
        TapReporting.generateReportForJsonFiles(filePath);
    }

    @Test
    public void testgenerateReportForJsonFilesDeviceFarm() throws IOException {
        String filePath = "target/test-classes/reports";
        TapReporting.generateReportForJsonFiles(filePath);
    }

    @Test
    public void testgenerateReportForJsonFilesDeviceId() throws IOException {
        String filePath = null;
        System.setProperty("device.udid", "cb12315627");
        FileReaderUtil.createDirectory(System.getProperty("user.dir") + "/reports/cucumber/cb12315627");
        FileReaderUtil.copyFilesToDirectory("target/test-classes/testFile", System.getProperty("user.dir") + "/reports/cucumber/cb12315627/");
        TapReporting.generateReportForJsonFiles(filePath);
    }


    @Test
    public void testDetailedReport() {
        FileReaderUtil.deleteFile("reports/detail-test-test-results.pdf");
        TapReporting.detailedReport("reports/cucumber/cucumber.json", "detail-report");
    }

}
