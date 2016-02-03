package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;
import com.infinityworks.webapp.rest.dto.VoterPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.infinityworks.webapp.common.lang.ListExtras.isNullOrEmpty;

/**
 * Service to search for electoral wards by ward and constituency name.
 */
@Service
class ElectorPreviewService implements PreviewService {

    private final ElectoralWardService electoralWardService;

    @Autowired
    public ElectorPreviewService(ElectoralWardService electoralWardService) {
        this.electoralWardService = electoralWardService;
    }

    @Override
    public Try<VoterPreview> previewElectorsByWards(ElectorPreviewRequest request) {

        Try<List<Ward>> wards;
        if (shouldRequestAllWards(request)) {
            wards = electoralWardService.findByConstituencyName(request.getConstituencyName());
        } else {
            wards = electoralWardService.findByConstituencyNameAndWardNames(request.getConstituencyName(), request.getWardNames());
        }

        return wards.map(w -> new VoterPreview(42, w));
    }

    private boolean shouldRequestAllWards(ElectorPreviewRequest electorRequest) {
        return isNullOrEmpty(electorRequest.getWardNames());
    }
}
