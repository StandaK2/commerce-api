package cz.rohlik.commerce.infrastructure.exception;

import cz.rohlik.commerce.infrastructure.exception.model.ErrorCode;
import cz.rohlik.commerce.infrastructure.exception.model.ErrorReason;

/** Standard error reason types for the Commerce API application. */
public final class ErrorReasonTypes {

    public static final ErrorReason PRODUCT_NOT_FOUND = CommerceErrorReason.PRODUCT_NOT_FOUND;
    public static final ErrorReason ORDER_NOT_FOUND = CommerceErrorReason.ORDER_NOT_FOUND;
    public static final ErrorReason INSUFFICIENT_STOCK = CommerceErrorReason.INSUFFICIENT_STOCK;
    public static final ErrorReason INVALID_ORDER_STATE = CommerceErrorReason.INVALID_ORDER_STATE;
    public static final ErrorReason ORDER_EXPIRED = CommerceErrorReason.ORDER_EXPIRED;
    public static final ErrorReason PRODUCT_HAS_ACTIVE_ORDERS =
            CommerceErrorReason.PRODUCT_HAS_ACTIVE_ORDERS;
    public static final ErrorReason ORDER_ITEM_NOT_FOUND = CommerceErrorReason.ORDER_ITEM_NOT_FOUND;
    public static final ErrorReason ORDER_ITEM_ALREADY_EXISTS =
            CommerceErrorReason.ORDER_ITEM_ALREADY_EXISTS;
    public static final ErrorReason ORDER_ITEM_NOT_BELONGS_TO_ORDER =
            CommerceErrorReason.ORDER_ITEM_NOT_BELONGS_TO_ORDER;

    private ErrorReasonTypes() {
        // Utility class
    }

    private enum CommerceErrorReason implements ErrorReason {
        PRODUCT_NOT_FOUND,
        ORDER_NOT_FOUND,
        INSUFFICIENT_STOCK,
        INVALID_ORDER_STATE,
        ORDER_EXPIRED,
        PRODUCT_HAS_ACTIVE_ORDERS,
        ORDER_ITEM_NOT_FOUND,
        ORDER_ITEM_ALREADY_EXISTS,
        ORDER_ITEM_NOT_BELONGS_TO_ORDER;

        @Override
        public ErrorCode getErrorCode() {
            return new ErrorCode(this.name());
        }
    }
}
