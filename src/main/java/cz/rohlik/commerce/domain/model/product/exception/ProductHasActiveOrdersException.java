package cz.rohlik.commerce.domain.model.product.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

public class ProductHasActiveOrdersException extends CommerceException {

    public ProductHasActiveOrdersException(UUID productId) {
        super(
                ErrorReasonTypes.PRODUCT_HAS_ACTIVE_ORDERS,
                String.format(
                        "Product with ID %s cannot be deleted because it has active orders",
                        productId));
    }
}
