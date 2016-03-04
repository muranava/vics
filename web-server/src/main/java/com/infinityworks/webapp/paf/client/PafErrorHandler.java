package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.error.PafApiFailure;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
public class PafErrorHandler {
    private static final String FORMAT = "Error calling PAF API. Method=%s. Response contained status code=%s, status text=%s";

    public <T> Try<T> handleError(String context, Exception e) {
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException exception = (HttpStatusCodeException) e;
            PafApiFailure failure = new PafApiFailure(String.format(FORMAT, context, exception.getStatusCode().value(), exception.getStatusText()), e);
            return Try.failure(failure);
        }
        return Try.failure(new PafApiFailure("Paf API call failed. Check log for errors", e));
    }
}
