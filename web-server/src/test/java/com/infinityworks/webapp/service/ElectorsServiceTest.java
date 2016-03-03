package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.domain.Constituency;
import com.infinityworks.webapp.domain.Role;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.paf.client.PafClient;
import com.infinityworks.webapp.paf.dto.ImmutableProperty;
import com.infinityworks.webapp.paf.dto.Property;
import com.infinityworks.webapp.pdf.CanvassTableConfig;
import com.infinityworks.webapp.pdf.DocumentBuilder;
import com.infinityworks.webapp.pdf.PDFTableGenerator;
import com.infinityworks.webapp.pdf.TableBuilder;
import com.infinityworks.webapp.pdf.model.GeneratedPdfTable;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import com.infinityworks.webapp.rest.dto.Street;
import com.lowagie.text.pdf.PdfPTable;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.infinityworks.webapp.testsupport.builder.ConstituencyBuilder.constituency;
import static com.infinityworks.webapp.testsupport.builder.UserBuilder.user;
import static com.infinityworks.webapp.testsupport.builder.WardBuilder.ward;
import static com.infinityworks.webapp.testsupport.builder.downstream.ElectorsByStreetsRequestBuilder.electorsByStreets;
import static com.infinityworks.webapp.testsupport.builder.downstream.StreetBuilder.street;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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
    private TableBuilder tableBuilder = new TableBuilder(new CanvassTableConfig());

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
        PDFTableGenerator pdfTableGenerator = (tableBuilder, voters1, wardCode, wardName, constituencyName) -> asList(table1, table2);
        underTest = new ElectorsService(pafClient, wardService, pdfTableGenerator);

        User user = user().withRole(Role.USER).build();

        List<Street> streets = asList(
                street().withMainStreet("Low Road").build(),
                street().withMainStreet("High Road").build()
        );
        ElectorsByStreetsRequest request = electorsByStreets().withStreets(streets).withFlags(null).build();
        List<List<Property>> voters = singletonList(asList(
                ImmutableProperty.builder().withStreet("1").withHouse("1").withPostCode("CV2 2DH").withPostTown("Coventry").withVoters(emptyList()).build(),
                ImmutableProperty.builder().withStreet("1").withHouse("2").withPostCode("CV2 2DH").withPostTown("Coventry").withVoters(emptyList()).build(),
                ImmutableProperty.builder().withStreet("1").withHouse("3").withPostCode("CV2 2DH").withPostTown("Coventry").withVoters(emptyList()).build()
        ));

        given(wardService.getByCode(w.getCode(), user)).willReturn(Try.success(w));
        given(pafClient.findElectorsByStreet(streets, w.getCode())).willReturn(Try.success(voters));
        List<GeneratedPdfTable> tables = asList(table1, table2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(4);
        given(documentBuilder.buildPages(tables)).willReturn(outputStream);

        Try<ByteArrayOutputStream> byStreets = underTest.getPdfOfElectorsByStreet(tableBuilder, documentBuilder, request, w.getCode(), user);

        assertThat(byStreets.isSuccess(), is(true));
    }
}