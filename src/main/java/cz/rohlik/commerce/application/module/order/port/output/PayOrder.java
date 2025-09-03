package cz.rohlik.commerce.application.module.order.port.output;

import java.util.UUID;

public interface PayOrder {
    String invoke(UUID orderId);
}
