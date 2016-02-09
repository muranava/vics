package com.infinityworks.webapp.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.*;

/**
 * An application wide error handler.
 */
public final class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Handles application errors and generates an error response
     *
     * @param exception the error to map to a response
     */
    public ResponseEntity<?> mapToResponseEntity(Exception exception) {

        if (exception instanceof ValidationException) {
            return ResponseEntity.status(BAD_REQUEST).body(createError(exception));
        }

        if (exception instanceof LoginFailure) {
            return ResponseEntity.status(UNAUTHORIZED).body(createError(exception));
        }

        if (exception instanceof BadCredentialsException) {
            ErrorEntity errorEntity = new ErrorEntity(LoginFailure.class.getSimpleName(), "Bad credentials");
            return ResponseEntity.status(UNAUTHORIZED).body(errorEntity);
        }

        if (exception instanceof AccessDeniedException) {
            ErrorEntity errorEntity = new ErrorEntity(exception.getClass().getSimpleName(), "Access denied");
            return ResponseEntity.status(FORBIDDEN).body(errorEntity);
        }

        if (exception instanceof NotFoundFailure) {
            return ResponseEntity.status(NOT_FOUND).body(createError(exception));
        }

        log.error("Error is not mapped", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Server error");
    }

    private static ErrorEntity createError(Exception exception) {
        return new ErrorEntity(exception.getClass().getSimpleName(), exception.getMessage());
    }
}
