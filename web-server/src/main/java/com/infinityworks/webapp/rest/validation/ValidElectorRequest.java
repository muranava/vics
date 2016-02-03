package com.infinityworks.webapp.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidElectorRequestValidator.class })
@Documented
public @interface ValidElectorRequest {

    String message() default "{com.infinityworks.webapp.rest.validation.rest.dto" +
            "ValidElectorRequest.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
