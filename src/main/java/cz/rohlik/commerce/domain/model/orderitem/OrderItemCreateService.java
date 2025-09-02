package cz.rohlik.commerce.domain.model.orderitem;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Domain service for creating OrderItem entities. Handles simple OrderItem creation logic. */
@Service
@RequiredArgsConstructor
public class OrderItemCreateService {

    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderItem create(UUID orderId, UUID productId, Integer quantity, BigDecimal unitPrice) {
        var orderItem = new OrderItem(orderId, productId, quantity, unitPrice);
        return orderItemRepository.save(orderItem);
    }
}
