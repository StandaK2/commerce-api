package cz.rohlik.commerce.application.module.order;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderExpirationServiceIT extends IntegrationTest {

    @Test
    void shouldExpireAllCreatedOrdersAndReleaseStock() {
        // Create products
        var product1 = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 100);
        var product2 = testDataHelper.createProduct("Keyboard", new BigDecimal("129.99"), 50);
        var product3 = testDataHelper.createProduct("Monitor", new BigDecimal("299.99"), 20);
        var product4 = testDataHelper.createProduct("Headset", new BigDecimal("199.99"), 30);

        // Create orders with different statuses
        var createdOrder1 = testDataHelper.createDefaultOrder(); // CREATED - will be expired
        var createdOrder2 = testDataHelper.createDefaultOrder(); // CREATED - will be expired
        var createdOrder3 = testDataHelper.createDefaultOrder(); // CREATED - will be expired
        var paidOrder = testDataHelper.createOrder(OrderStatus.PAID); // PAID - will NOT be expired

        // Create order items for CREATED orders
        var createdOrder1Item1 =
                testDataHelper.createOrderItem(
                        createdOrder1.getId(), product1.getId(), 5, product1.getPrice());
        var createdOrder1Item2 =
                testDataHelper.createOrderItem(
                        createdOrder1.getId(), product2.getId(), 3, product2.getPrice());

        var createdOrder2Item1 =
                testDataHelper.createOrderItem(
                        createdOrder2.getId(), product3.getId(), 2, product3.getPrice());
        var createdOrder2Item2 =
                testDataHelper.createOrderItem(
                        createdOrder2.getId(), product4.getId(), 1, product4.getPrice());

        // Create order items for third CREATED order
        var createdOrder3Item =
                testDataHelper.createOrderItem(
                        createdOrder3.getId(), product1.getId(), 2, product1.getPrice());

        // Create order items for paid order (should not be expired due to status)
        var paidOrderItem =
                testDataHelper.createOrderItem(
                        paidOrder.getId(), product2.getId(), 1, product2.getPrice());

        // Reserve stock for all order items
        product1.reserveStock(7); // 5 from createdOrder1 + 2 from createdOrder3
        product2.reserveStock(4); // 3 from createdOrder1 + 1 from paidOrder
        product3.reserveStock(2); // 2 from createdOrder2
        product4.reserveStock(1); // 1 from createdOrder2

        // Verify initial state
        assertThat(product1.getStockQuantity()).isEqualTo(93); // 100 - 7
        assertThat(product2.getStockQuantity()).isEqualTo(46); // 50 - 4
        assertThat(product3.getStockQuantity()).isEqualTo(18); // 20 - 2
        assertThat(product4.getStockQuantity()).isEqualTo(29); // 30 - 1

        // Flush to ensure orders are persisted before modifying timestamps
        entityManager.flush();

        // Set orders' updated_at to the past to simulate age beyond expiration threshold
        var oldInstant = Instant.now().minus(2, ChronoUnit.HOURS);
        setOrderUpdatedAt(createdOrder1.getId(), oldInstant);
        setOrderUpdatedAt(createdOrder2.getId(), oldInstant);
        setOrderUpdatedAt(createdOrder3.getId(), oldInstant);
        setOrderUpdatedAt(paidOrder.getId(), oldInstant);

        // Clear persistence context to avoid version conflicts
        entityManager.clear();

        // Execute expiration service (should expire only CREATED orders older than threshold)
        orderExpirationService.expireCreatedOrders();

        // Verify all CREATED orders are expired due to age
        var expiredOrder1 = orderRepository.findById(createdOrder1.getId()).orElseThrow();
        var expiredOrder2 = orderRepository.findById(createdOrder2.getId()).orElseThrow();
        var expiredOrder3 = orderRepository.findById(createdOrder3.getId()).orElseThrow();

        assertThat(expiredOrder1.getStatus()).isEqualTo(OrderStatus.EXPIRED);
        assertThat(expiredOrder2.getStatus()).isEqualTo(OrderStatus.EXPIRED);
        assertThat(expiredOrder3.getStatus()).isEqualTo(OrderStatus.EXPIRED);

        // Verify paid order is still PAID (not affected by expiration)
        var stillPaidOrder = orderRepository.findById(paidOrder.getId()).orElseThrow();
        assertThat(stillPaidOrder.getStatus()).isEqualTo(OrderStatus.PAID);

        // Verify stock was released for all CREATED orders
        var finalProduct1 = productRepository.findById(product1.getId()).orElseThrow();
        var finalProduct2 = productRepository.findById(product2.getId()).orElseThrow();
        var finalProduct3 = productRepository.findById(product3.getId()).orElseThrow();
        var finalProduct4 = productRepository.findById(product4.getId()).orElseThrow();

        assertThat(finalProduct1.getStockQuantity())
                .isEqualTo(100); // 93 + 7 (5 + 2 from createdOrder1 and createdOrder3)
        assertThat(finalProduct2.getStockQuantity())
                .isEqualTo(49); // 46 + 3 (from createdOrder1 only, paidOrder not expired)
        assertThat(finalProduct3.getStockQuantity()).isEqualTo(20); // 18 + 2 (from createdOrder2)
        assertThat(finalProduct4.getStockQuantity()).isEqualTo(30); // 29 + 1 (from createdOrder2)
    }

    @Test
    void shouldNotExpireOrdersWhenTheyAreAlreadyPaid() {
        // Create products and paid order
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 100);
        var paidOrder = testDataHelper.createOrder(OrderStatus.PAID);
        var orderItem =
                testDataHelper.createOrderItem(
                        paidOrder.getId(), product.getId(), 5, product.getPrice());

        product.reserveStock(5);
        var initialStock = product.getStockQuantity();

        // Execute expiration service (paid orders should not be affected)
        orderExpirationService.expireCreatedOrders();

        // Verify paid order is still PAID
        var stillPaidOrder = orderRepository.findById(paidOrder.getId()).orElseThrow();
        assertThat(stillPaidOrder.getStatus()).isEqualTo(OrderStatus.PAID);

        // Verify stock was not released for paid order
        var finalProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(finalProduct.getStockQuantity()).isEqualTo(initialStock);
    }

    @Test
    void shouldHandleEmptyOrdersList() {
        // Execute expiration service with no orders in database
        orderExpirationService.expireCreatedOrders();

        // Should complete without errors
        assertThat(orderRepository.findAll()).isEmpty();
    }

    private void setOrderUpdatedAt(UUID orderId, Instant instant) {
        jdbcTemplate.update(
                "UPDATE \"order\" SET updated_at = ? WHERE id = ?",
                Timestamp.from(instant),
                orderId);
    }
}
