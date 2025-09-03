package cz.rohlik.commerce.application.module.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.order.command.CancelOrderCommand;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.order.exception.InvalidOrderStateException;
import cz.rohlik.commerce.domain.model.order.exception.OrderNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CancelOrderCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldCancelOrderAndReleaseStockForMultipleItems() {
        // Create products
        var product1 = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 100);
        var product2 = testDataHelper.createProduct("Keyboard", new BigDecimal("129.99"), 50);
        var product3 = testDataHelper.createProduct("Monitor", new BigDecimal("299.99"), 20);
        var product4 = testDataHelper.createProduct("Headset", new BigDecimal("199.99"), 30);

        // Create order
        var order = testDataHelper.createDefaultOrder();

        // Create multiple order items with different products
        var orderItem1 =
                testDataHelper.createOrderItem(
                        order.getId(), product1.getId(), 5, product1.getPrice());
        var orderItem2 =
                testDataHelper.createOrderItem(
                        order.getId(), product2.getId(), 3, product2.getPrice());
        var orderItem3 =
                testDataHelper.createOrderItem(
                        order.getId(), product3.getId(), 2, product3.getPrice());
        var orderItem4 =
                testDataHelper.createOrderItem(
                        order.getId(), product4.getId(), 1, product4.getPrice());

        // Reserve stock to simulate order items affecting inventory
        product1.reserveStock(5);
        product2.reserveStock(3);
        product3.reserveStock(2);
        product4.reserveStock(1);

        // Verify initial stock levels
        var initialProduct1Stock =
                productRepository.findById(product1.getId()).orElseThrow().getStockQuantity();
        var initialProduct2Stock =
                productRepository.findById(product2.getId()).orElseThrow().getStockQuantity();
        var initialProduct3Stock =
                productRepository.findById(product3.getId()).orElseThrow().getStockQuantity();
        var initialProduct4Stock =
                productRepository.findById(product4.getId()).orElseThrow().getStockQuantity();

        assertThat(initialProduct1Stock).isEqualTo(95); // 100 - 5
        assertThat(initialProduct2Stock).isEqualTo(47); // 50 - 3
        assertThat(initialProduct3Stock).isEqualTo(18); // 20 - 2
        assertThat(initialProduct4Stock).isEqualTo(29); // 30 - 1

        var result = commandBus.execute(new CancelOrderCommand(order.getId()));

        assertThat(result.id()).isEqualTo(order.getId());

        // Verify order status changed to CANCELLED
        var cancelledOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(cancelledOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        // Verify stock was released for all products
        var finalProduct1 = productRepository.findById(product1.getId()).orElseThrow();
        var finalProduct2 = productRepository.findById(product2.getId()).orElseThrow();
        var finalProduct3 = productRepository.findById(product3.getId()).orElseThrow();
        var finalProduct4 = productRepository.findById(product4.getId()).orElseThrow();

        assertThat(finalProduct1.getStockQuantity()).isEqualTo(100); // 95 + 5
        assertThat(finalProduct2.getStockQuantity()).isEqualTo(50); // 47 + 3
        assertThat(finalProduct3.getStockQuantity()).isEqualTo(20); // 18 + 2
        assertThat(finalProduct4.getStockQuantity()).isEqualTo(30); // 29 + 1
    }

    @Test
    void shouldFailWhenOrderNotFound() {
        var nonExistentOrderId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(() -> commandBus.execute(new CancelOrderCommand(nonExistentOrderId)))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @ParameterizedTest
    @EnumSource(
            value = OrderStatus.class,
            names = {"PAID", "CANCELLED", "EXPIRED"})
    void shouldFailWhenOrderNotInCreatedStatus(OrderStatus invalidStatus) {
        var order = testDataHelper.createOrder(invalidStatus);

        assertThatThrownBy(() -> commandBus.execute(new CancelOrderCommand(order.getId())))
                .isInstanceOf(InvalidOrderStateException.class);
    }
}
