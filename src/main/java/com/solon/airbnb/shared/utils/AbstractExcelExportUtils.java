package com.solon.airbnb.shared.utils;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractExcelExportUtils {
	
	protected XSSFWorkbook workbook;
	protected XSSFSheet sheet;
    
    protected abstract void createHeaderRow();
    protected abstract void writeData();
    
    public AbstractExcelExportUtils() {
    	workbook = new XSSFWorkbook();
    }
    
	protected void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer){
            cell.setCellValue((Integer) value);
        }else if (value instanceof Double){
            cell.setCellValue((Double) value);
        }else if (value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Long){
            cell.setCellValue((Long) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    
    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
