package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.common.lang.Try.Failure;
import com.infinityworks.webapp.error.PafApiFailure;
import org.springframework.stereotype.Component;

@Component
public class PafErrorHandler {
    public Try<?> handleError(String context, Exception e) {
        return Try.failure(new PafApiFailure(""));
    }
}
