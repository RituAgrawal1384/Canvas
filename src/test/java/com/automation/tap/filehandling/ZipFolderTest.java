package com.automation.tap.filehandling;

import org.testng.annotations.Test;

public class ZipFolderTest {


    @Test
    public void testGetJsonString() {
        ZipFolder.zipFilesAndFolder("reports/cucumber","reports/cucumber.zip");
    }


}
