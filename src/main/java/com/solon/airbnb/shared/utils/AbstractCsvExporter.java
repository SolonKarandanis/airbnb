package com.solon.airbnb.shared.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCsvExporter {
    private static Logger log = LoggerFactory.getLogger(AbstractCsvExporter.class);
	
	protected XSSFWorkbook workbook;
	protected XSSFSheet sheet;
    
    protected abstract void createHeaderRow();
    protected abstract void writeData();
    
    public AbstractCsvExporter() {
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

    public byte[] exportDataToByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            createHeaderRow();
            writeData();
            workbook.write(baos);
            workbook.close();
        } catch (IOException ioex) {
            log.error(ioex.getMessage(), ioex);
        }
        return baos.toByteArray();

    }

}
