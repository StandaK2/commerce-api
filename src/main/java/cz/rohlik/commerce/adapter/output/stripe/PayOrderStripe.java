package cz.rohlik.commerce.adapter.output.stripe;

import cz.rohlik.commerce.application.module.order.port.output.PayOrder;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Stripe implementation of PayOrder port. Simulates payment processing through Stripe payment
 * gateway.
 */
@Service
public class PayOrderStripe implements PayOrder {

    @Override
    public String invoke(UUID orderId) {
        // Simulate payment processing with Stripe
        // In real implementation, this would call Stripe API and return actual payment ID
        return "pi_3QK1234567890abcdef";
    }
}
