package cz.rohlik.commerce.domain.model.product.exception;

import cz.rohlik.commerce.infrastructure.exception.CommerceException;
import cz.rohlik.commerce.infrastructure.exception.ErrorReasonTypes;
import java.util.UUID;

/** Exception thrown when a product is not found. */
public class ProductNotFoundException extends CommerceException {

    public ProductNotFoundException(UUID productId) {
        super(
                ErrorReasonTypes.PRODUCT_NOT_FOUND,
                String.format("Product with ID %s not found", productId));
    }
}
