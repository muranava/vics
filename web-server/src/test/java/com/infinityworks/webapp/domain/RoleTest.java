package com.infinityworks.webapp.domain;

import com.infinityworks.common.lang.Try;
import org.junit.Test;

import javax.validation.ValidationException;

import static com.infinityworks.webapp.testsupport.matcher.TryFailureMatcher.isFailure;
import static com.infinityworks.webapp.testsupport.matcher.TrySuccessMatcher.isSuccess;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class RoleTest {

    @Test
    public void returnsSuccessIfCanConvertFromString() throws Exception {
        Try<Role> admin = Role.of("ADMIN");

        assertThat(admin, isSuccess(equalTo(Role.ADMIN)));
    }

    @Test
    public void returnsFailureIfCannotConvertFromString() throws Exception {
        Try<Role> admin = Role.of("I DONT EXIST");

        assertThat(admin, isFailure(instanceOf(ValidationException.class)));
    }
}
