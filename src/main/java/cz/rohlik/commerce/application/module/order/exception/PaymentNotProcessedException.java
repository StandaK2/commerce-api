package cz.rohlik.commerce.application.module.order.exception;

import java.util.UUID;

public class PaymentNotProcessedException extends RuntimeException {

    public PaymentNotProcessedException(UUID orderId) {
        super(String.format("Payment for order %s could not be processed", orderId));
    }
}
