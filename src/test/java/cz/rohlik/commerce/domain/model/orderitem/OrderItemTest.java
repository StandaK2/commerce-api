package cz.rohlik.commerce.domain.model.orderitem;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.TestUtils;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithCorrectValues() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);
        var orderItem = new OrderItem(orderId, productId, 5, new BigDecimal("12.50"));

        assertEquals(orderId, orderItem.getOrderId());
        assertEquals(productId, orderItem.getProductId());
        assertEquals(5, orderItem.getQuantity());
        assertEquals(new BigDecimal("12.50"), orderItem.getUnitPrice());
    }

    @Test
    void shouldCalculateSubtotalCorrectly() {
        var orderId = TestUtils.createUuidFromNumber(1);
        var productId = TestUtils.createUuidFromNumber(2);

        // Test single item
        var singleItem = new OrderItem(orderId, productId, 1, new BigDecimal("9.99"));
        assertEquals(new BigDecimal("9.99"), singleItem.getSubtotal());

        // Test multiple items
        var multipleItems = new OrderItem(orderId, productId, 3, new BigDecimal("15.25"));
        assertEquals(new BigDecimal("45.75"), multipleItems.getSubtotal());

        // Test large quantity
        var largeQuantity = new OrderItem(orderId, productId, 100, new BigDecimal("2.50"));
        assertEquals(new BigDecimal("250.00"), largeQuantity.getSubtotal());
    }
}
