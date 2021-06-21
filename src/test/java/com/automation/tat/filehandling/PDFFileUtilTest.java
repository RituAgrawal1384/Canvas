package com.automation.tat.filehandling;

import org.apache.pdfbox.pdmodel.PDPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PDFFileUtilTest {
    private PdfFileUtil pdfFileUtil = new PdfFileUtil();

    @Test
    public void testpdfImageCompareAndWriteDifferenceInPDF() {
        String file1Path = "/testFile/pdfcompare/First.pdf";
        String file2Path = "/testFile/pdfcompare/Second.pdf";
        String outfile1Path = "reports/pdfDifference";
        boolean match=pdfFileUtil.pdfImageCompareAndWriteDifferenceInPDF(file1Path,file2Path,outfile1Path);
        Assert.assertNotNull(match);
    }

    @Test
    public void testgetPdfTextByPageNumber() {
        String file1Path = "/testFile/pdfcompare/First.pdf";

        String text=pdfFileUtil.getPdfTextByPageNumberAsInputStream(file1Path,1);
        Assert.assertNotNull(text);
    }

    @Test
    public void testgetPdfPageByPageNumber() {
        String file1Path = "/testFile/pdfcompare/First.pdf";

        PDPage text=pdfFileUtil.getPdfPageByPageNumber(file1Path,1);
        Assert.assertNotNull(text);
    }

    @Test
    public void tesgetPDFText() {
        String file1Path = "target/test-classes/testFile/pdfcompare/First.pdf";
        String[] text=pdfFileUtil.getPDFTextLineByLine(file1Path);
        Assert.assertNotNull(text);
    }

}
