package cz.rohlik.commerce.domain.model.order.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when an order is not found. */
public class OrderNotFoundException extends CommerceException {

    public OrderNotFoundException(UUID orderId) {
        super(
                ErrorReasonTypes.ORDER_NOT_FOUND,
                String.format("Order with ID %s not found", orderId));
    }
}
