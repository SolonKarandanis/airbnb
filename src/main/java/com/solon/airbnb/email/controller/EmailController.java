package com.solon.airbnb.email.controller;

import com.solon.airbnb.email.application.dto.EmailDTO;
import com.solon.airbnb.email.application.dto.EmailSearchRequestDTO;
import com.solon.airbnb.email.application.dto.EnumTypeDTO;
import com.solon.airbnb.email.application.service.EmailService;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.AirbnbException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    protected EmailService emailService;

    @PreAuthorize("hasPermission('SEARCH_EMAILS')")
    @GetMapping("/types")
    public ResponseEntity<List<EnumTypeDTO>> getEmailTypes() {
        List<EmailType> emailTypes = emailService.getEmailTypes();
        List<EnumTypeDTO> dtos = new ArrayList<>();
        if (emailTypes != null) {
            emailTypes.forEach(et -> dtos.add(new EnumTypeDTO(et.getId(), et.getResourceKey())));
        }
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<Void> resendEmails(
            @NotEmpty(message = "{error.search.email.no.emails.selected}") @RequestBody List<Integer> emailIds)
            throws AirbnbException {
        emailService.resendEmails(emailIds);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResults<EmailDTO>> searchEmails(@Valid @RequestBody EmailSearchRequestDTO searchRequest)
            throws AirbnbException{
        return null;
    }
}
