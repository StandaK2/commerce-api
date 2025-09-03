package cz.rohlik.commerce.domain.model.orderitem.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when an order item is not found. */
public class OrderItemNotFoundException extends CommerceException {

    public OrderItemNotFoundException(UUID orderItemId) {
        super(
                ErrorReasonTypes.ORDER_ITEM_NOT_FOUND,
                String.format("Order item with ID %s not found", orderItemId));
    }
}
