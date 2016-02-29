package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.commondto.VotersByStreet;
import com.infinityworks.pdfgen.DocumentBuilder;
import com.infinityworks.pdfgen.model.GeneratedPdfTable;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.pdf.PDFRenderer;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.Street;
import com.infinityworks.webapp.service.client.PafClient;
import com.lowagie.text.pdf.PdfPTable;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.infinityworks.testsupport.builder.PropertyBuilder.property;
import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.ElectorsByStreetsRequestBuilder.electorsByStreets;
import static com.infinityworks.webapp.testsupport.builder.StreetBuilder.street;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * TODO This test needs to be reworked and cover error scenarios
 */
public class ElectorsServiceTest {

    private ElectorsService underTest;
    private PafClient pafClient;
    private DocumentBuilder documentBuilder;
    private WardService wardService;

    @Before
    public void setUp() throws Exception {
        pafClient = mock(PafClient.class);
        wardService = mock(WardService.class);
        documentBuilder = mock(DocumentBuilder.class);
    }

    @Test
    public void returnsTheElectorsByStreetRawData() throws Exception {
        Constituency c = constituency().build();
        Ward w = ward().withWardCode("E0911334").withConstituency(c).build();
        GeneratedPdfTable table1 = new GeneratedPdfTable(new PdfPTable(1), "Low Road", w.getName(), w.getCode(), w.getConstituency().getName());
        GeneratedPdfTable table2 = new GeneratedPdfTable(new PdfPTable(1), "High Road", w.getName(), w.getCode(), w.getConstituency().getName());
        PDFRenderer pdfRenderer = (voters1, wardCode, wardName, constituencyName) -> asList(table1, table2);
        underTest = new ElectorsService(pafClient, wardService, pdfRenderer, mock(DocumentBuilder.class));

        User user = user().withRole(Role.USER).build();

        List<Street> streets = asList(
                street().withMainStreet("Low Road").build(),
                street().withMainStreet("High Road").build()
        );
        ElectorsByStreetsRequest request = electorsByStreets().withStreets(streets).withFlags(null).build();
        List<VotersByStreet> voters = singletonList(new VotersByStreet(asList(
                property().withBuildingNumber("1").build(),
                property().withBuildingNumber("2").build(),
                property().withBuildingNumber("3").build()
        )));

        given(wardService.getByCode(w.getCode(), user)).willReturn(Try.success(w));
        given(pafClient.findElectorsByStreet(streets, w.getCode())).willReturn(Try.success(voters));
        List<GeneratedPdfTable> tables = asList(table1, table2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4);
        given(documentBuilder.buildPages(tables)).willReturn(outputStream);

        Try<ByteArrayOutputStream> byStreets = underTest.electorsByStreets(request, w.getCode(), user);

        assertThat(byStreets.isSuccess(), is(true));
    }
}