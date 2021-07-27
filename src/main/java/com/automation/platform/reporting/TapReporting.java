package com.automation.platform.reporting;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import com.automation.platform.filehandling.FileReaderUtil;
import com.automation.platform.filehandling.ZipFolder;
import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberFeatureOverview;
import com.github.mkolisnyk.cucumber.reporting.CucumberUsageReporting;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class TapReporting {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipFolder.class);

    public static void generateReportForJsonFiles(String devicefarmLogDir) {

        String logFileDirectory = null;
        String reportDirectory = null;
        String projectName = "Test Automation ";

        if (devicefarmLogDir != null) {
            logFileDirectory = devicefarmLogDir;
            LOGGER.info("log directory is :" + logFileDirectory);
            FileReaderUtil.copyDirectory("reports/cucumber", devicefarmLogDir);
            reportDirectory = "reports/cucumber";
        } else {
            if (System.getProperty("device.udid") != null) {
                FileReaderUtil.createDirectory("reports/cucumber" + System.getProperty("device.udid"));

                logFileDirectory = "reports/cucumber" + System.getProperty("device.udid");
                reportDirectory = "reports/cucumber/" + System.getProperty("device.udid");
                projectName = projectName + "-" + System.getProperty("device.udid");
            } else {
                logFileDirectory = "reports/cucumber";
                reportDirectory = "reports/cucumber";
            }
        }

        File reportOutputDirectory = new File(logFileDirectory);
        List<String> jsonFiles = getAllJsonFilesUnderTarget(reportDirectory);
        assertTrue(jsonFiles.size() > 0);
        String buildNumber = "1";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName + Configvariable.globalPropertyMap.get("project.name"));
        configuration.setRunWithJenkins(false);
        configuration.setBuildNumber(buildNumber);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

    }

    private static List<String> getAllJsonFilesUnderTarget(String folderLocation) {

        List<String> jsonFiles = new ArrayList<>();
        File directory = new File(folderLocation);
        File[] files = directory.listFiles((file, name) -> name.endsWith(".json"));
        if (files != null && files.length > 0) {
            for (File f : files) {
                String filePath = folderLocation + "/" + f.getName();
                jsonFiles.add(filePath);

                Path path = Paths.get(filePath);
                File file = path.toFile();

                assertTrue(file.exists());
                LOGGER.info(String.format("Found json file: %s with size of %d at path %s",
                        filePath, file.length(), file.getAbsolutePath()));
            }
        }
        return jsonFiles;
    }

    public static void overviewReport(String jsonReportFilePath, String overViewreportName) {
        CucumberFeatureOverview results = new CucumberFeatureOverview();
        results.setOutputDirectory("reports");
        results.setOutputName(overViewreportName);
        results.setSourceFile(jsonReportFilePath);
        try {
            results.execute(true, new String[]{"pdf", "png"});
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to generate overview report [{}]", e.getMessage());
        }
    }

    public static void detailedReport(String jsonReportFilePath, String detailReportName) {
        CucumberDetailedResults results = new CucumberDetailedResults();
        results.setOutputDirectory("reports/");
        results.setOutputName(detailReportName);
        results.setSourceFile(jsonReportFilePath);
        try {
            results.execute(true, new String[]{"pdf", "png"});
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to generate detailed report [{}]", e.getMessage());
        }
    }

    public static void usageReport(String usageJsonReportPath, String usageReportName) {
        CucumberUsageReporting results = new CucumberUsageReporting();
        results.setOutputDirectory("reports");
        results.setOutputName(usageReportName);
        results.setSourceFile(usageJsonReportPath);
        results.setJsonUsageFiles(new String[]{"reports/cucumber-usage.json"});
        try {
            results.execute(new String[]{"pdf", "png"});
        } catch (Exception e) {
            throw new TapException(TapExceptionType.PROCESSING_FAILED, "Not able to generate usage report [{}]", e.getMessage());
        }
    }
}