package com.solon.airbnb.user.application.utils;

import java.util.List;

import com.solon.airbnb.user.application.dto.ReadUserDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.solon.airbnb.shared.utils.AbstractCsvExporter;
import com.solon.airbnb.user.domain.User;

public class UserCsvExporter extends AbstractCsvExporter {
	
	private List<ReadUserDTO> userList;
	

	public UserCsvExporter(List<ReadUserDTO> userList) {
		super();
		this.userList = userList;
	}

	@Override
	protected void createHeaderRow() {
		sheet   = workbook.createSheet("User Results");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "User Results", style);
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

	        for (ReadUserDTO user : userList){
	            Row row = sheet.createRow(rowCount++);
	            int columnCount = 0;
	            createCell(row, columnCount++, user.publicId(), style);
	            createCell(row, columnCount++, user.firstName(), style);
	            createCell(row, columnCount++, user.lastName(), style);
	            createCell(row, columnCount++, user.email(), style);
	            createCell(row, columnCount++, user.status(), style);
	        }
	}

}
