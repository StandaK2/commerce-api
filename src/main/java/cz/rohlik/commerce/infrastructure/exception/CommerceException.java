package cz.rohlik.commerce.infrastructure.exception;

import cz.rohlik.commerce.infrastructure.exception.model.ApiException;
import cz.rohlik.commerce.infrastructure.exception.model.ErrorCode;
import cz.rohlik.commerce.infrastructure.exception.model.ErrorReason;
import cz.rohlik.commerce.infrastructure.exception.model.Source;

/** Base exception for all Commerce API exceptions. */
public abstract class CommerceException extends ApiException {

    protected CommerceException(ErrorReason reason, String message) {
        super(ErrorSource.INSTANCE, reason, message);
    }

    private enum ErrorSource implements Source {
        INSTANCE;

        @Override
        public ErrorCode getErrorCode() {
            return new ErrorCode("COMMERCE");
        }
    }
}
