package com.infinityworks.webapp.rest.validation;

import com.google.common.base.Strings;
import com.infinityworks.webapp.rest.dto.SearchElectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;

public class SearchElectorsRequestValidator implements ConstraintValidator<ValidSearchElectorsRequest, SearchElectors> {
    @Override
    public void initialize(ValidSearchElectorsRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(SearchElectors value, ConstraintValidatorContext context) {
        return !asList(value.getFirstName(), value.getLastName(), value.getAddress(), value.getPostCode())
                .stream().allMatch(Strings::isNullOrEmpty);
    }
}
