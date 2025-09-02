package cz.rohlik.commerce.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static cz.rohlik.commerce.infrastructure.config.LoggingConfig.logger;

/**
 * Global exception handler for the Commerce API application.
 * Provides centralized error handling and response formatting.

 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = logger();

    @ExceptionHandler(CommerceException.class)
    public ResponseEntity<ErrorResponse> handleApiException(CommerceException exception) {
        // Always log API exceptions
        logger.error("API Exception occurred: {}", exception.getMessage(), exception);

        var status = AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        var httpStatus = status != null ? status.code() : HttpStatus.BAD_REQUEST;

        var errorResponse = new ErrorResponse(
                Instant.now(),
                httpStatus.value(),
                exception.getReason().getErrorCode().value(),
                exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        // This handles validation errors from @Valid commands in CommandBus
        logger.warn("Command validation failed: {}", exception.getMessage());

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Invalid command parameters: " + exception.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        logger.error("Unexpected exception occurred", exception);

        var errorResponse = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "An unexpected error occurred"
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
