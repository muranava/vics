package com.infinityworks.webapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.PafClient;
import com.infinityworks.webapp.clients.paf.PafRequestExecutor;
import com.infinityworks.webapp.clients.paf.dto.GotvVoterRequest;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.converter.GotpvRequestAdapter;
import com.infinityworks.webapp.converter.VoterAddressConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.error.NotFoundFailure;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.AddressLabelGenerator;
import com.infinityworks.webapp.pdf.AveryLabelGenerator;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.infinityworks.webapp.config.Config.objectMapper;

@Service
public class LabelService {
    private final Logger log = LoggerFactory.getLogger(LabelService.class);
    private final PafClient pafClient;
    private final PafRequestExecutor pafRequestExecutor;
    private final VoterAddressConverter voterAddressConverter;
    private final AddressLabelGenerator addressLabelGenerator;
    private final GotpvRequestAdapter gotpvRequestAdapter;
    private final WardService wardService;

    @Autowired
    public LabelService(PafClient pafClient,
                        PafRequestExecutor pafRequestExecutor,
                        VoterAddressConverter voterAddressConverter,
                        AveryLabelGenerator addressLabelGenerator,
                        GotpvRequestAdapter gotpvRequestAdapter,
                        WardService wardService) {
        this.pafClient = pafClient;
        this.pafRequestExecutor = pafRequestExecutor;
        this.voterAddressConverter = voterAddressConverter;
        this.addressLabelGenerator = addressLabelGenerator;
        this.gotpvRequestAdapter = gotpvRequestAdapter;
        this.wardService = wardService;
    }

    public Try<ByteArrayOutputStream> generateAddressLabelsForPostalVoters(ElectorsByStreetsRequest request, String wardCode, User user) {
        return wardService
                .getByCode(wardCode, user)
                .flatMap(ward -> {
                    GotvVoterRequest gotvVoterRequest = gotpvRequestAdapter.apply(request);
                    Call<PropertyResponse> call = pafClient.filteredVotersByStreets(wardCode, gotvVoterRequest);
                    Try<PropertyResponse> properties = pafRequestExecutor.execute(call);

                    log.debug("User={} Requested PDF of filtered voters for ward={} numStreets={} for GOTPV. filter={}",
                            user, ward.getCode(), request.getStreets().size(), serializeRequest(gotvVoterRequest));

                    return properties.flatMap(response -> {
                        List<AddressLabel> addressLabels = voterAddressConverter.apply(response);

                        if (addressLabels.isEmpty()) {
                            return Try.failure(new NotFoundFailure("No voters matching the filter criteria"));
                        } else {
                            return addressLabelGenerator.generateAddressLabels(addressLabels);
                        }
                    });
                });
    }

    private String serializeRequest(GotvVoterRequest request) {
        try {
            return objectMapper.writeValueAsString(request.filter());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize request");
            return "";
        }
    }
}
