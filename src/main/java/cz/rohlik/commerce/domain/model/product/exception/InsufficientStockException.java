package cz.rohlik.commerce.domain.model.product.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when trying to reserve more stock than available. */
public class InsufficientStockException extends CommerceException {

    public InsufficientStockException(
            UUID productId, String productName, Integer availableStock, Integer requestedQuantity) {
        super(
                ErrorReasonTypes.INSUFFICIENT_STOCK,
                String.format(
                        "Insufficient stock for product '%s' (ID: %s). Available: %d, Requested: %d",
                        productName, productId, availableStock, requestedQuantity));
    }
}
