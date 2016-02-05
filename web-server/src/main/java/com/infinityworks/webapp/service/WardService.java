package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Ward;
import com.infinityworks.webapp.rest.dto.ElectorsByWardAndConstituencyRequest;
import com.infinityworks.webapp.rest.dto.VoterPreview;

import java.util.List;

/**
 * Finds all the wards within the given constituency
 */
public interface WardService {
    Try<List<Ward>> findByConstituencyName(String name);

    Try<VoterPreview> findElectorsByWard(ElectorsByWardAndConstituencyRequest request);

    Try<List<Ward>> findByConstituencyNameAndWardNames(String constituencyName, List<String> wardNames);
}
