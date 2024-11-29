package com.solon.airbnb.user.controller;

import java.net.URISyntaxException;

import com.solon.airbnb.shared.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.user.application.dto.JwtDTO;
import com.solon.airbnb.user.application.dto.SubmitCredentialsDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.AuthService;
import com.solon.airbnb.user.application.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
public class AuthController extends GenericController{
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private AuthService authService;
    private JwtService jwtService;

    public AuthController( AuthService authService,JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * {@code POST  /reports} : Create a new report.
     *
     * @param submitCredentialsDTO submit credentials.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     * with body the new JwtDTO, or with status {@code 400 (Bad Request)} if the
     * case has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<JwtDTO> authenticate(@Valid @RequestBody SubmitCredentialsDTO submitCredentialsDTO) throws BusinessException {
    	log.info("AuthController->authenticate----------->username: {}   password: {}",submitCredentialsDTO.getUsername(),submitCredentialsDTO.getPassword());
    	UserDTO authenticatedUser = authService.authenticate(submitCredentialsDTO);
    	JwtDTO jwt = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok().body(jwt);
    }
}
