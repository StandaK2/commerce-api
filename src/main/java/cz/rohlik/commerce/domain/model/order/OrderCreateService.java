package cz.rohlik.commerce.domain.model.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Domain service for creating Order entities. Handles simple Order creation logic. */
@Service
@RequiredArgsConstructor
public class OrderCreateService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order create() {
        var order = new Order();
        return orderRepository.save(order);
    }
}
