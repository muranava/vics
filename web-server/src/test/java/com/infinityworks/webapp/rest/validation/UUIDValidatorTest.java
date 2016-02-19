package com.infinityworks.webapp.rest.validation;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class UUIDValidatorTest {

    private final UUIDValidator underTest = new UUIDValidator();

    @Test
    public void returnsFalseIfInvalid() throws Exception {
        boolean isValid = underTest.isValid("invalid UUID", mock(ConstraintValidatorContext.class));

        assertThat(isValid, is(false));
    }

    @Test
    public void returnsTrueIfUUID() throws Exception {
        boolean isValid = underTest.isValid(UUID.randomUUID().toString(), mock(ConstraintValidatorContext.class));

        assertThat(isValid, is(true));
    }
}
