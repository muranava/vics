package com.infinityworks.webapp.security;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.rest.dto.Credentials;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SecurityUtilsTest {
    @Test
    public void returnsTheCredentialsFromTheAuthHeader() throws Exception {
        String header = "Basic c3RlaW46cGFzc3dvcmQ=";

        Try<Credentials> credentialsTry = SecurityUtils.credentialsFromAuthHeader(header);

        assertThat(credentialsTry.isSuccess(), is(true));
        assertThat(credentialsTry.get().getUsername(), is("stein"));
        assertThat(credentialsTry.get().getPassword(), is("password"));
    }

    @Test
    public void returnsFailureIfAuthHeaderIsJunk() throws Exception {
        String header = "Basic 8wgq8wgweg0qweg=";

        Try<Credentials> credentialsTry = SecurityUtils.credentialsFromAuthHeader(header);

        assertThat(credentialsTry.isSuccess(), is(false));
    }

    @Test
    public void returnsFailureWhenPasswordIsNull() throws Exception {
        String header = "Basic YWRtaW46";

        Try<Credentials> credentialsTry = SecurityUtils.credentialsFromAuthHeader(header);

        assertThat(credentialsTry.isSuccess(), is(false));
    }

    @Test
    public void returnsFailureWhenUsernameIsNull() throws Exception {
        String header = "Basic OmFzZGFkc2FzZA==";

        Try<Credentials> credentialsTry = SecurityUtils.credentialsFromAuthHeader(header);

        assertThat(credentialsTry.isSuccess(), is(false));
    }
}