package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;
import com.infinityworks.webapp.rest.dto.VoterPreview;
import org.junit.Before;
import org.junit.Test;

import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ElectorPreviewServiceTest {

    private ElectoralWardService electoralWardService;
    private ElectorPreviewService underTest;

    @Before
    public void setUp() throws Exception {
        electoralWardService = mock(ElectoralWardService.class);
        underTest = new ElectorPreviewService(electoralWardService);
    }

    @Test
    public void returnsAllWardsIfNoWardsSpecified() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(null, "Coventry South");
        given(electoralWardService.findByConstituencyName(request.getConstituencyName()))
                .willReturn(Try.success(
                        singletonList(ward().withConstituencyName("Spon End").build())));

        Try<VoterPreview> result = underTest.previewElectorsByWards(request);

        assertThat(result.get().getWards(), hasItem(ward().withConstituencyName("Spon End").build()));
    }

    @Test
    public void returnsSpecificWardsIfWardsSpecified() throws Exception {
        ElectorPreviewRequest request = new ElectorPreviewRequest(singletonList("Tile Hill"), "Coventry South");
        given(electoralWardService.findByConstituencyNameAndWardNames(request.getConstituencyName(), request.getWardNames()))
                .willReturn(Try.success(
                        singletonList(ward().withConstituencyName("Tile Hill").build())));

        Try<VoterPreview> result = underTest.previewElectorsByWards(request);

        assertThat(result.get().getWards(), hasItem(ward().withConstituencyName("Tile Hill").build()));
    }
}