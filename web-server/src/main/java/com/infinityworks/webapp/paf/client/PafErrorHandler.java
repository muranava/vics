package com.infinityworks.webapp.paf.client;

import com.infinityworks.common.lang.Try;
import com.infinityworks.webapp.error.PafApiFailure;
import com.infinityworks.webapp.error.PafApiNotFoundFailure;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
public class PafErrorHandler {
    private static final String FORMAT = "Error calling PAF API. Method=%s. Response contained status code=%s, status text=%s";

    public <T> Try<T> handleError(String context, Exception e) {
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException exception = (HttpStatusCodeException) e;
            String msg = String.format(FORMAT, context, exception.getStatusCode().value(), exception.getStatusText());
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Try.failure(new PafApiNotFoundFailure(msg));
            } else {
                PafApiFailure failure = new PafApiFailure(msg, e);
                return Try.failure(failure);
            }
        }
        return Try.failure(new PafApiFailure("Paf API call failed. Check log for errors", e));
    }
}
