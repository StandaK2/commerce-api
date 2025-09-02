package cz.rohlik.commerce.infrastructure.exception.model;

/**
 * Base API exception class for the Commerce API application.

 * All API exceptions are logged by the global exception handler.
 */
public abstract class ApiException extends RuntimeException {
    private final Source source;
    private final ErrorReason reason;

    protected ApiException(Source source, ErrorReason reason, String message) {
        super(message);
        this.source = source;
        this.reason = reason;
    }

    public Source getSource() {
        return source;
    }

    public ErrorReason getReason() {
        return reason;
    }
}
