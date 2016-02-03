package com.infinityworks.webapp.rest.validation;

import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.infinityworks.webapp.common.lang.ListExtras.isNullOrEmpty;
import static com.infinityworks.webapp.common.lang.StringExtras.isNullOrEmpty;

public class ValidElectorRequestValidator implements ConstraintValidator<ValidElectorRequest, ElectorPreviewRequest> {
    @Override
    public void initialize(ValidElectorRequest ignored) {
    }

    @Override
    public boolean isValid(ElectorPreviewRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return false;
        } else {
            return !isNullOrEmpty(request.getConstituencyName()) &&
                   (isNullOrEmpty(request.getWardNames()) || request.getWardNames().stream().allMatch(w -> w != null));
        }
    }
}
