package com.infinityworks.webapp.rest.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<IsUUID, String> {
    @Override
    public void initialize(IsUUID constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException arg) {
            return false;
        }
        return true;
    }
}
