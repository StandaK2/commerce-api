package cz.rohlik.commerce.infrastructure.scheduled;

import cz.rohlik.commerce.application.module.order.OrderExpirationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderExpirationTrigger {

    private final OrderExpirationService orderExpirationService;

    @Scheduled(cron = "${commerce.order.expiration.check.cron:0 */5 * * * *}")
    public void trigger() {
        orderExpirationService.expireCreatedOrders();
    }
}
