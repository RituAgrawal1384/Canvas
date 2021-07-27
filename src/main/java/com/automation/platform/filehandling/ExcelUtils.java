package com.automation.platform.filehandling;

import com.automation.platform.config.Configvariable;
import com.automation.platform.exception.TapException;
import com.automation.platform.exception.TapExceptionType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class ExcelUtils {

    @Autowired
    private Configvariable configvariable;

    private FileInputStream file = null;

    private XSSFWorkbook workbook = null;

    private XSSFSheet sheet = null;


    public XSSFWorkbook getWorkBook(String filePath, int sheetNum) {
        //Create Workbook instance holding reference to .xlsx file
        try {
            file = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(sheetNum);
        } catch (FileNotFoundException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "File not found [{}]", filePath);
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "NOt able to create workbook for [{}]", filePath);
        }

        return workbook;

    }

    public Map<String, Integer> getExcelColumns(String filePath, int sheetNum) {
        Map<String, Integer> columnsIndex = new HashMap();
        getWorkBook(filePath, sheetNum);
        int cols = sheet.getRow(0).getLastCellNum();
        for (int iCellCount = 0; iCellCount < cols; iCellCount++) {
            Cell cellHeader = sheet.getRow(0).getCell(iCellCount);
            columnsIndex.put(cellHeader.getStringCellValue(), iCellCount);
        }
        return columnsIndex;
    }

    public void closeWorkBookAndFile() {
        try {
            file.close();
            workbook.close();
        } catch (IOException e) {
            throw new TapException(TapExceptionType.IO_ERROR, "NOt able to close workbook");
        }
    }

    public Map<String, Map<String, String>> readExcelDataIntoMap(String filePath, int sheetNum, String columnName) {
        Map<String, Map<String, String>> dataMap = new HashMap<>();
        try {
            Map<String, Integer> columns = getExcelColumns(filePath, sheetNum);

            int rows = sheet.getLastRowNum();
            int cols = columns.size();

            for (int iRow = 1; iRow <= rows; iRow++) {
                Map<String, String> rowData = new HashMap<>();
                Cell cell = sheet.getRow(iRow).getCell(columns.get(columnName));
                for (int iCellCount = 0; iCellCount < cols; iCellCount++) {
                    String cellVal = "";
                    Cell cellData = sheet.getRow(iRow).getCell(iCellCount);
                    Cell cellHeader = sheet.getRow(0).getCell(iCellCount);
                    if (!cellHeader.getStringCellValue().equalsIgnoreCase(columnName)) {
                        if (cellData != null) {
                            if (cellData.getCellType() == CellType.STRING) {
                                cellVal = cellData.getStringCellValue().toString();
                            } else if (cellData.getCellType() == CellType.NUMERIC) {
                                cellVal = Double.toString((int) cellData.getNumericCellValue());
                            }
                        }
                        rowData.put(configvariable.expandValue(cellHeader.getStringCellValue()), configvariable.expandValue(cellVal));
                    }
                }
                dataMap.put(configvariable.expandValue(cell.getStringCellValue()), rowData);
            }
            closeWorkBookAndFile();
            return dataMap;
        } catch (Exception e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to read Excel data into Map");
        }
    }


    public void createNewExcel(String sheetName, Map<String, Map<String, String>> dataMap, String filePath) {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //Iterate over data and write to sheet
        Set<String> keyset = dataMap.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Map<String, String> objArr = dataMap.get(key);
            int cellnum = 0;
            for (String obj : objArr.keySet()) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(objArr.get(obj));
            }
        }
        try {
            //Write the workbook in file system
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

        } catch (Exception e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to write to Excel data from Map");
        }

    }

    public void writeToColumnsForGivenRow(String filePath, int sheetNum, String rowToSearchVal, Map<String, String> rowData) {
        try {
            FileInputStream file = new FileInputStream(new File(filePath));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(sheetNum);

            int rows = sheet.getLastRowNum();
            int cols = sheet.getRow(0).getLastCellNum();

            for (int iRow = 1; iRow <= rows; iRow++) {
                Cell cell = sheet.getRow(iRow).getCell(0);
                if (rowToSearchVal.equals(cell.getStringCellValue())) {
                    for (String obj : rowData.keySet()) {
                        for (int iCellCount = 1; iCellCount < cols; iCellCount++) {
                            Cell cellHeader = sheet.getRow(0).getCell(iCellCount);
                            if (cellHeader.getStringCellValue().equalsIgnoreCase(configvariable.expandValue(obj))) {
                                String cellVal = configvariable.expandValue(rowData.get(configvariable.expandValue(obj)));
                                Cell cell2Update = sheet.getRow(iRow).getCell(iCellCount);
                                if (cell2Update == null) {
                                    cell2Update = sheet.getRow(iRow).createCell(iCellCount);
                                }
                                cell2Update.setCellValue(cellVal);
                            }

                        }
                    }
                }
            }
            file.close();
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to update row [{}]", rowToSearchVal);
        }
    }

    public void writeToRowsForGivenColumn(String filePath, int sheetNum, String columnName, String columnNameToSearch, Map<String, String> rowData) {
        try {
            Map<String, Integer> columns = getExcelColumns(filePath, sheetNum);

            int rows = sheet.getLastRowNum();
            int cols = columns.size();
            String[] listOfColumns = columnName.split("##");

            for (int iRow = 1; iRow <= rows; iRow++) {
                Cell cell = sheet.getRow(iRow).getCell(columns.get(columnNameToSearch));

                for (String obj : rowData.keySet()) {
                    if (configvariable.expandValue(obj).equals(cell.getStringCellValue())) {
                        for (int i = 0; i < listOfColumns.length; i++) {
                            for (int iCellCount = 1; iCellCount < cols; iCellCount++) {
                                Cell cellHeader = sheet.getRow(0).getCell(iCellCount);
                                if (cellHeader.getStringCellValue().equalsIgnoreCase(configvariable.expandValue(listOfColumns[i]))) {
                                    String cellVal = configvariable.expandValue(rowData.get(obj).split("##")[i]);
                                    Cell cell2Update = sheet.getRow(iRow).getCell(iCellCount);
                                    cell2Update = sheet.getRow(iRow).createCell(iCellCount);
                                    cell2Update.setCellValue(cellVal);
                                    break;
                                }

                            }
                        }
                    }
                }
            }
            file.close();
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to update rows [{}] for column [{}]", rowData, columnName);
        }
    }

    public void writeToAllRowsForGivenColumn(String filePath, int sheetNum, String columnName) {
        try {
            FileInputStream file = new FileInputStream(new File(filePath));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(sheetNum);

            int rows = sheet.getLastRowNum();
            int cols = sheet.getRow(0).getLastCellNum();

            String[] columns = columnName.split("##");

            for (int iRow = 1; iRow <= rows; iRow++) {
                for (int iCount = 0; iCount < columns.length; iCount++) {
                    for (int iCellCount = 0; iCellCount < cols; iCellCount++) {
                        Cell cellHeader = sheet.getRow(0).getCell(iCellCount);
                        if (cellHeader.getStringCellValue().equalsIgnoreCase(configvariable.expandValue(columns[iCount]))) {
                            Cell cell2Update = sheet.getRow(iRow).getCell(iCellCount);
                            if (cell2Update == null) {
                                cell2Update = sheet.getRow(iRow).createCell(iCellCount);
                            }
                            String cellVal = configvariable.expandValue(cell2Update.getStringCellValue());
                            cell2Update.setCellValue(cellVal);
                            break;
                        }
                    }

                }

            }
            file.close();
            FileOutputStream outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            throw new TapException(TapExceptionType.IO_ERROR, "Failed to update rows for column [{}]", columnName);
        }
    }

}
