package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import com.infinityworks.webapp.rest.dto.ElectorsByWardsRequest;

import java.util.List;

public interface PrintService {
    Try<List<ElectorResponse>> findPafEnrichedElectors(ElectorsByWardsRequest request);
    Try<List<ElectorResponse>> findLocalElectors(ElectorsByWardsRequest request);
}
