package cz.rohlik.commerce.domain.model.orderitem.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when an order item already exists for the given order and product. */
public class OrderItemAlreadyExistsException extends CommerceException {

    public OrderItemAlreadyExistsException(UUID orderId, UUID productId) {
        super(
                ErrorReasonTypes.ORDER_ITEM_ALREADY_EXISTS,
                String.format(
                        "Order item already exists for order %s and product %s",
                        orderId, productId));
    }
}
