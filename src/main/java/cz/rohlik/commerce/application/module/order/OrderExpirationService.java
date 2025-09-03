package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.application.module.orderitem.OrderItemFinderService;
import cz.rohlik.commerce.application.module.orderitem.OrderItemService;
import cz.rohlik.commerce.domain.model.order.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderExpirationService {

    private final OrderFinderService orderFinderService;
    private final OrderItemFinderService orderItemFinderService;
    private final OrderItemService orderItemService;

    @Value("${commerce.order.expiration.time-minutes:30}")
    private int expirationTimeInMinutes;

    public void expireCreatedOrders() {
        List<Order> expiredOrders =
                orderFinderService.findAllExpiredCreatedOrders(expirationTimeInMinutes);

        for (Order order : expiredOrders) {
            var orderItems = orderItemFinderService.findAllByOrderId(order.getId());
            orderItemService.releaseStockForOrderItems(orderItems);
            order.markAsExpired();
        }
    }
}
