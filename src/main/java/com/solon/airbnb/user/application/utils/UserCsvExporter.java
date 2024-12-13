package com.solon.airbnb.user.application.utils;

import java.util.List;

import com.opencsv.CSVWriter;
import com.solon.airbnb.shared.utils.AbstractCsvExporter;
import com.solon.airbnb.user.application.dto.ReadUserDTO;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractCsvExporter {
	
	private List<ReadUserDTO> userList;
	

	public UserCsvExporter(List<ReadUserDTO> userList, HttpServletResponse response) {
		super(response);
		this.userList = userList;
	}

	@Override

    protected void createHeaderRow(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[] { "Id", "First Name", "Last Name", "Email" }, false);
    }

	@Override
	protected void writeData(CSVWriter csvWriter) {
        for (ReadUserDTO bean : userList) {
            String[] line = { bean.publicId(), bean.firstName(), bean.lastName(), bean.email()};
            csvWriter.writeNext(line, false);
        }
	}

}
