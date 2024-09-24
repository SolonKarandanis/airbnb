package com.solon.airbnb.user.application.validation;

import com.solon.airbnb.user.repository.AuthorityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class AuthorityValidator implements ConstraintValidator<Authority, String> {

    private final  AuthorityRepository repository;

    public AuthorityValidator(AuthorityRepository repository){
        this.repository=repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            return repository.existsById(value);
        }
        return true;
    }
}
