package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.domain.model.order.Order;
import cz.rohlik.commerce.domain.model.order.OrderRepository;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.order.exception.OrderNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderFinderService {

    private final OrderRepository orderRepository;

    public Order findById(UUID orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> findAllExpiredCreatedOrders(int expirationTimeInMinutes) {
        Instant expirationThreshold =
                Instant.now().minus(expirationTimeInMinutes, ChronoUnit.MINUTES);
        return orderRepository.findAllByStatusAndUpdatedAtBefore(
                OrderStatus.CREATED, expirationThreshold);
    }
}
