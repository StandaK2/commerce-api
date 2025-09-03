package cz.rohlik.commerce.domain.model.orderitem.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when an order item does not belong to the specified order. */
public class OrderItemNotBelongsToOrderException extends CommerceException {

    public OrderItemNotBelongsToOrderException(UUID orderItemId, UUID orderId) {
        super(
                ErrorReasonTypes.ORDER_ITEM_NOT_BELONGS_TO_ORDER,
                String.format(
                        "Order item with ID %s does not belong to order with ID %s",
                        orderItemId, orderId));
    }
}
