package com.solon.airbnb.user.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.user.application.utils.UserExcelExportUtils;
import com.solon.airbnb.user.domain.User;

import jakarta.servlet.http.HttpServletResponse;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
public class UsersController extends GenericController{
	
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);
	
	
	@GetMapping("/export-to-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Customers_Information.xlsx";
        response.setHeader(headerKey, headerValue);
        List<User> allUsers = usersService.findAllUsers();
        UserExcelExportUtils exportUtils = new UserExcelExportUtils(allUsers);
    }

}
