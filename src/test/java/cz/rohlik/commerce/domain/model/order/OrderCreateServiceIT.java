package cz.rohlik.commerce.domain.model.order;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import org.junit.jupiter.api.Test;

class OrderCreateServiceIT extends IntegrationTest {

    @Test
    void shouldCreateOrderSuccessfully() {
        assertEquals(0, orderRepository.count());

        var order = orderCreateService.create();

        assertNotNull(order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());

        assertFalse(order.isExpired(30));

        assertEquals(1, orderRepository.count());

        var savedOrder = orderRepository.findById(order.getId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(order.getStatus(), savedOrder.getStatus());
    }
}
