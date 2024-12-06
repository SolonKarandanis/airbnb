package com.solon.airbnb.user.application.utils;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.solon.airbnb.shared.utils.AbstractCsvExporter;
import com.solon.airbnb.user.domain.User;

public class UserCsvExporter extends AbstractCsvExporter {
	
	private List<User> userList;
	

	public UserCsvExporter(List<User> userList) {
		super();
		this.userList = userList;
	}

	@Override
	protected void createHeaderRow() {
		sheet   = workbook.createSheet("Customer Information");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Customer Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "First Name", style);
        createCell(row, 2, "Last Name", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Country", style);
        createCell(row, 5, "State", style);
        createCell(row, 6, "City", style);
        createCell(row, 7, "Address", style);
		
	}

	@Override
	protected void writeData() {
		 int rowCount = 2;
	        CellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        font.setFontHeight(14);
	        style.setFont(font);

	        for (User user : userList){
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	            createCell(row, columnCount++, user.getUsername(), style);
	            createCell(row, columnCount++, user.getFirstName(), style);
	            createCell(row, columnCount++, user.getLastName(), style);
	            createCell(row, columnCount++, user.getEmail(), style);
	            createCell(row, columnCount++, user.getStatus(), style);
	        }
	}

}
