package com.infinityworks.webapp.service;

import com.infinityworks.webapp.common.Try;
import com.infinityworks.webapp.rest.dto.VoterPreview;
import com.infinityworks.webapp.rest.dto.ElectorPreviewRequest;

public interface PreviewService {
    Try<VoterPreview> previewElectorsByWards(ElectorPreviewRequest voterRequest);
}
