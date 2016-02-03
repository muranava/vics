package com.infinityworks.webapp.rest.validation;

import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ValidElectorPreviewRequestValidatorTest {
    private final ValidElectorRequestValidator underTest = new ValidElectorRequestValidator();
    private final ConstraintValidatorContext ctx = mock(ConstraintValidatorContext.class);

    @Test
    public void returnFalseIfRequestIsNull() throws Exception {
        assertThat(underTest.isValid(null, ctx), is(false));
    }

    @Test
    public void returnFalseIfConstituencyNameIsNull() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(null, null);

        boolean valid = underTest.isValid(request, ctx);

        assertThat(valid, is(false));
    }

    @Test
    public void returnFalseIfConstituencyNameIsEmpty() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("Coventry South", "Coventry North"), "");

        boolean valid = underTest.isValid(request, ctx);

        assertThat(valid, is(false));
    }

    @Test
    public void returnTrueIfAllPresent() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("Tile Hill", "Canley"), "Coventry North");

        boolean valid = underTest.isValid(request, ctx);

        assertThat(valid, is(true));
    }

    @Test
    public void returnTrueIfWardEmptyAndConstituencyPresent() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(emptyList(), "Coventry North");

        boolean valid = underTest.isValid(request, ctx);

        assertThat(valid, is(true));
    }

    @Test
    public void returnFalseIfWardListContainsNullElement() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(asList("A", null, "B"), "Coventry North");

        boolean valid = underTest.isValid(request, ctx);

        assertThat(valid, is(false));
    }
}
