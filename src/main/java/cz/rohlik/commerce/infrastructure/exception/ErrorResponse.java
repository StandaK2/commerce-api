package cz.rohlik.commerce.infrastructure.exception;

import java.time.Instant;

/**
 * Standardized error response for API exceptions.

 *
 * @param timestamp When the error occurred
 * @param status HTTP status code
 * @param error Error code string
 * @param message Human-readable error message
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message
) {
}
