package cz.rohlik.commerce.domain.model.orderitem;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.IntegrationTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderItemCreateServiceIT extends IntegrationTest {

    @Test
    void shouldCreateOrderItemSuccessfully() {
        assertEquals(0, orderItemRepository.count());

        var product = testDataHelper.createDefaultProduct();
        var order = testDataHelper.createDefaultOrder();

        var orderItem =
                orderItemCreateService.create(
                        order.getId(), product.getId(), 3, new BigDecimal("15.50"));

        assertNotNull(orderItem.getId());
        assertEquals(order.getId(), orderItem.getOrderId());
        assertEquals(product.getId(), orderItem.getProductId());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(new BigDecimal("15.50"), orderItem.getUnitPrice());
        assertEquals(new BigDecimal("46.50"), orderItem.getSubtotal());

        assertEquals(1, orderItemRepository.count());

        var savedOrderItem = orderItemRepository.findById(orderItem.getId()).orElse(null);
        assertNotNull(savedOrderItem);
        assertEquals(orderItem.getOrderId(), savedOrderItem.getOrderId());
        assertEquals(orderItem.getProductId(), savedOrderItem.getProductId());
        assertEquals(orderItem.getQuantity(), savedOrderItem.getQuantity());
        assertEquals(orderItem.getUnitPrice(), savedOrderItem.getUnitPrice());
    }
}
