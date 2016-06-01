package com.infinityworks.pdfserver.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.pafclient.PafClient;
import com.infinityworks.pafclient.PafRequestExecutor;
import com.infinityworks.pafclient.dto.GotvVoterRequest;
import com.infinityworks.pafclient.dto.PropertyResponse;
import com.infinityworks.pdfserver.controller.dto.GeneratePdfRequest;
import com.infinityworks.pdfserver.converter.GotpvRequestAdapter;
import com.infinityworks.pdfserver.converter.VoterAddressConverter;
import com.infinityworks.pdfserver.error.NotFoundFailure;
import com.infinityworks.pdfserver.pdf.AddressLabel;
import com.infinityworks.pdfserver.pdf.AveryLabelGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class AddressLabelGenerator {
    private final Logger log = LoggerFactory.getLogger(AddressLabelGenerator.class);
    private final PafClient pafClient;
    private final PafRequestExecutor pafRequestExecutor;
    private final VoterAddressConverter voterAddressConverter;
    private final AveryLabelGenerator addressLabelGenerator;
    private final GotpvRequestAdapter gotpvRequestAdapter;

    @Autowired
    public AddressLabelGenerator(PafClient pafClient,
                                 PafRequestExecutor pafRequestExecutor,
                                 VoterAddressConverter voterAddressConverter,
                                 AveryLabelGenerator addressLabelGenerator,
                                 GotpvRequestAdapter gotpvRequestAdapter) {
        this.pafClient = pafClient;
        this.pafRequestExecutor = pafRequestExecutor;
        this.voterAddressConverter = voterAddressConverter;
        this.addressLabelGenerator = addressLabelGenerator;
        this.gotpvRequestAdapter = gotpvRequestAdapter;
    }

    public Try<ByteArrayOutputStream> generateAddressLabelsForPostalVoters(GeneratePdfRequest request) {
        GotvVoterRequest gotvVoterRequest = gotpvRequestAdapter.apply(request);
        Call<PropertyResponse> call = pafClient.filteredVotersByStreets(request.getInfo().getWardCode(), gotvVoterRequest);
        Try<PropertyResponse> properties = pafRequestExecutor.execute(call);

        log.debug("Requested address labels for ward={} numStreets={} for GOTPV", request.getInfo().getWardCode(), request.getStreets().size());

        return properties.flatMap(response -> {
            List<AddressLabel> addressLabels = voterAddressConverter.apply(response);
            if (addressLabels.isEmpty()) {
                return Try.failure(new NotFoundFailure("No voters matching the filter criteria"));
            } else {
                return addressLabelGenerator.generateAddressLabels(addressLabels);
            }
        });
    }
}
