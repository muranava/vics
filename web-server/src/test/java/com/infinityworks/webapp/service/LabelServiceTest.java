package com.infinityworks.webapp.service;

import com.infinityworks.webapp.clients.pdfserver.PdfClient;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LabelServiceTest {
    private LabelService underTest;
    private WardService wardService;
    private PdfClient pdfClient;

    @Before
    public void setUp() throws Exception {
        wardService = mock(WardService.class);
        pdfClient = mock(PdfClient.class);
        underTest = new LabelService(wardService, pdfClient);
    }

    @Test
    public void generatesTheAddressLabels() throws Exception {

    }
}
