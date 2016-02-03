package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.domain.Elector;
import com.infinityworks.webapp.rest.dto.ElectorResponse;
import com.infinityworks.webapp.rest.dto.PrintElectorsRequest;

import java.util.List;

public interface PrintService {
    Try<List<ElectorResponse>> printElectors(PrintElectorsRequest request);
}
