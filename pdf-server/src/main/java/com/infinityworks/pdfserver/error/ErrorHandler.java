package com.infinityworks.pdfserver.error;

import com.infinityworks.pafclient.error.PafApiFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public final class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);
    private static final String ERROR_MESSAGE = "Something failed. Contact your system administrator";

    public ResponseEntity<?> mapToResponse(Exception exception, HttpHeaders headers) {
        if (exception instanceof PafApiFailure) {
            log.error(exception.getMessage(), exception);
            ErrorResponse ErrorResponse = new ErrorResponse(PafApiFailure.class.getSimpleName(), ERROR_MESSAGE);
            return new ResponseEntity<>(ErrorResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (exception instanceof NotFoundFailure) {
            log.error(exception.getMessage(), exception);
            ErrorResponse ErrorResponse = new ErrorResponse(PafApiFailure.class.getSimpleName(), exception.getMessage());
            return new ResponseEntity<>(ErrorResponse, headers, HttpStatus.NOT_FOUND);
        }

        log.error("Error is not mapped", exception);
        return new ResponseEntity<>(ERROR_MESSAGE, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> mapToResponse(Exception exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return mapToResponse(exception, headers);
    }
}
