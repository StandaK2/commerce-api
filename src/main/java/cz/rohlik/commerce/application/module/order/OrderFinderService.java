package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.domain.model.order.Order;
import cz.rohlik.commerce.domain.model.order.OrderRepository;
import cz.rohlik.commerce.domain.model.order.exception.OrderNotFoundException;
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
}
