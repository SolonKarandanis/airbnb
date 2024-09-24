package com.solon.airbnb.infrastructure.config.authorization;

import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.AccountStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDTO user = (UserDTO) userDetailsService.loadUserByUsername(username);
        log.info("CustomAuthProvider->authenticate->user: {}" , user);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        if(Boolean.FALSE.equals(user.getVerified())){
            throw new BadCredentialsException("User not verified");
        }

        if(isAccountNonActive(user.getStatus())){
            throw new BadCredentialsException("User not active");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Boolean isAccountNonActive(AccountStatus status){
        if(AccountStatus.INACTIVE.equals(status) || AccountStatus.DELETED.equals(status)){
            return true;
        }
        return false;
    }
}
