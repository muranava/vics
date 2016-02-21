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
public final class RestErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(RestErrorHandler.class);
    private static final String VAGUE_ERROR_RESPONSE = "Something failed. Contact your system administrator";

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
            ErrorEntity errorEntity = new ErrorEntity(LoginFailure.class.getSimpleName(), "Bad credentials", "");
            return ResponseEntity.status(UNAUTHORIZED).body(errorEntity);
        }

        if (exception instanceof AccessDeniedException) {
            ErrorEntity errorEntity = new ErrorEntity(exception.getClass().getSimpleName(), "Access denied", "");
            return ResponseEntity.status(FORBIDDEN).body(errorEntity);
        }

        if (exception instanceof NotAuthorizedFailure) {
            return ResponseEntity.status(UNAUTHORIZED).body(createError(exception));
        }

        if (exception instanceof NotFoundFailure) {
            return ResponseEntity.status(NOT_FOUND).body(createError(exception));
        }

        if (exception instanceof UserSessionFailure) {
            log.error("User session failure", exception);
            ErrorEntity errorEntity = new ErrorEntity(LoginFailure.class.getSimpleName(), VAGUE_ERROR_RESPONSE, "");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorEntity);
        }

        if (exception instanceof BadRequestFailure) {
            return ResponseEntity.status(BAD_REQUEST).body(createError(exception));
        }

        if (exception instanceof IllegalStateException) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Something failed. Check logs for errors");
        }

        if (exception instanceof PafApiFailure) {
            ErrorEntity errorEntity = new ErrorEntity(PafApiFailure.class.getSimpleName(), VAGUE_ERROR_RESPONSE, "");
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorEntity);
        }

        log.error("Error is not mapped", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(VAGUE_ERROR_RESPONSE);
    }

    private static ErrorEntity createError(Exception exception) {
        String custom;
        if (exception instanceof CanvassError) {
            custom = ((CanvassError) exception).getCustom();
        } else {
            custom = "";
        }
        return new ErrorEntity(exception.getClass().getSimpleName(), exception.getMessage(), custom);
    }
}
