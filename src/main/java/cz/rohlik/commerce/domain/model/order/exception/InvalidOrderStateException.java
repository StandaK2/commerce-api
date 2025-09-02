package cz.rohlik.commerce.domain.model.order.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when trying to perform an invalid state transition on an order. */
public class InvalidOrderStateException extends CommerceException {

    public InvalidOrderStateException(UUID orderId, String message) {
        super(
                ErrorReasonTypes.INVALID_ORDER_STATE,
                String.format("Invalid operation on order %s: %s", orderId, message));
    }
}
