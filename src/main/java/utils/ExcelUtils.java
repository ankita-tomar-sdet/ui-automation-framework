package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class ExcelUtils {
	
	 // ThreadLocal ensures thread-safety during parallel execution
    private static ThreadLocal<Integer> currentRow = new ThreadLocal<>();
    
    // Fetch Test data from the excel sheet and return as a list of maps (column name -> cell value)
    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String columnName = headerRow.getCell(j).toString();
                    String cellValue = (currentRow.getCell(j) != null) ? currentRow.getCell(j).toString() : "";
                    rowMap.put(columnName, cellValue);
                }
                data.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    // Set the current row index for the test case being executed
    public static void setCurrentRow(int row) {
        currentRow.set(row);
    }

    // Get the current row index for the test case being executed
    public static int getCurrentRow() {
        return currentRow.get();
    }

    // Update a specific cell value based on the current row and column name
    public static void updateCell(String filePath, String sheetName, String columnName, String newValue) {
        int rowIndex = getCurrentRow(); // Automatically gets the row for the current test
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int colIndex = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                if (headerRow.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
                    colIndex = i;
                    break;
                }
            }

            if (colIndex != -1) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);
                Cell cell = row.getCell(colIndex);
                if (cell == null) cell = row.createCell(colIndex);
                
                cell.setCellValue(newValue);

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}