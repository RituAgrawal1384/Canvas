//package com.automation.platform.filehandling;
//
//import com.automation.platform.config.TapBeansLoad;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.testng.annotations.BeforeSuite;
//import org.testng.annotations.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ComponentScan(basePackages = {"com.automation.platform"})
//@Configuration
//public class ExcelUtilsTest {
//
//    private ExcelUtils excelUtils;
//
//    @BeforeSuite
//    public void setup() {
//        TapBeansLoad.setConfigClass(ExcelUtilsTest.class);
//        TapBeansLoad.init();
//        excelUtils = (ExcelUtils) TapBeansLoad.getBean(ExcelUtils.class);
//    }
//
//
//    @Test
//    public void testReadExcelDataIntoMap() {
//        excelUtils.readExcelDataIntoMap(System.getProperty("user.dir") + "/src/test/resources/testfile/testverificationlink.xlsx", 0, "");
//    }
//
//    @Test
//    public void testWriteToExistingExcelFile() {
//        Map<String, String> rowData = new HashMap<>();
//        rowData.put("Values", "Pending");
//        excelUtils.writeToColumnsForGivenRow(System.getProperty("user.dir") + "/src/test/resources/testfile/testverificationlink.xlsx", 0, "Policy Status", rowData);
//    }
//}
