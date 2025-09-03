package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.domain.model.orderitem.OrderItem;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemRepository;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemFinderService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem findById(UUID orderItemId) {
        return orderItemRepository
                .findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(orderItemId));
    }

    @Transactional
    public void deleteById(UUID orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}
