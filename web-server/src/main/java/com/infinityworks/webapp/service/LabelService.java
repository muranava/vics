package com.infinityworks.webapp.service;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.clients.paf.command.GetVotersCommand;
import com.infinityworks.webapp.clients.paf.command.GetVotersCommandFactory;
import com.infinityworks.webapp.clients.paf.converter.PafStreetRequestConverter;
import com.infinityworks.webapp.clients.paf.dto.PafStreetRequest;
import com.infinityworks.webapp.clients.paf.dto.PropertyResponse;
import com.infinityworks.webapp.converter.VoterAddressConverter;
import com.infinityworks.webapp.domain.User;
import com.infinityworks.webapp.pdf.AddressLabel;
import com.infinityworks.webapp.pdf.AddressLabelGenerator;
import com.infinityworks.webapp.pdf.AveryLabelGenerator;
import com.infinityworks.webapp.rest.dto.ElectorsByStreetsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LabelService {
    private final GetVotersCommandFactory getVotersCommandFactory;
    private final PafStreetRequestConverter pafStreetRequestConverter;
    private final VoterAddressConverter voterAddressConverter;
    private final AddressLabelGenerator addressLabelGenerator;
    private final WardService wardService;

    @Autowired
    public LabelService(GetVotersCommandFactory getVotersCommandFactory,
                        PafStreetRequestConverter pafStreetRequestConverter,
                        VoterAddressConverter voterAddressConverter,
                        AveryLabelGenerator addressLabelGenerator,
                        WardService wardService) {
        this.getVotersCommandFactory = getVotersCommandFactory;
        this.pafStreetRequestConverter = pafStreetRequestConverter;
        this.voterAddressConverter = voterAddressConverter;
        this.addressLabelGenerator = addressLabelGenerator;
        this.wardService = wardService;
    }

    public Try<ByteArrayOutputStream> generateLabelsPdf(ElectorsByStreetsRequest request, String wardCode, User user) {
        return wardService
                .getByCode(wardCode, user)
                .flatMap(ward -> {
                    List<PafStreetRequest> pafStreets = request.getStreets()
                            .stream()
                            .map(pafStreetRequestConverter)
                            .collect(toList());

                    GetVotersCommand votersCommand = getVotersCommandFactory.create(pafStreets, wardCode);
                    Try<PropertyResponse> properties = votersCommand.execute();
                    return properties.flatMap(response -> {
                        List<AddressLabel> addressLabels = voterAddressConverter.apply(response);
                        return addressLabelGenerator.generateAddressLabels(addressLabels);
                    });
                });
    }
}
