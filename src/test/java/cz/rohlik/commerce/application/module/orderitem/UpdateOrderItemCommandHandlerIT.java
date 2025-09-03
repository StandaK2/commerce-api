package cz.rohlik.commerce.application.module.orderitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.orderitem.command.UpdateOrderItemCommand;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotBelongsToOrderException;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotFoundException;
import cz.rohlik.commerce.domain.model.product.exception.InsufficientStockException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class UpdateOrderItemCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldUpdateOrderItem() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order = testDataHelper.createDefaultOrder();
        var orderItem =
                testDataHelper.createOrderItem(
                        order.getId(), product.getId(), 5, product.getPrice());

        // Manually reserve initial stock to simulate existing order item
        product.reserveStock(5);
        var initialOrderUpdatedAt = order.getUpdatedAt();

        var result =
                commandBus.execute(new UpdateOrderItemCommand(order.getId(), orderItem.getId(), 8));

        assertThat(result.id()).isEqualTo(orderItem.getId());

        var updatedOrderItem = orderItemRepository.findById(orderItem.getId()).orElseThrow();
        assertThat(updatedOrderItem.getQuantity()).isEqualTo(8);
        assertThat(updatedOrderItem.getUnitPrice())
                .isEqualTo(new BigDecimal("79.99")); // Price immutable

        // Verify additional stock was reserved (25 - 5 - 3 = 17)
        var updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(17);

        // Verify order timestamp was updated
        var updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getUpdatedAt()).isAfter(initialOrderUpdatedAt);
    }

    @Test
    void shouldFailWhenOrderItemNotFound() {
        var nonExistentOrderItemId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new UpdateOrderItemCommand(
                                                TestUtils.createUuidFromNumber(1),
                                                nonExistentOrderItemId,
                                                10)))
                .isInstanceOf(OrderItemNotFoundException.class);
    }

    @Test
    void shouldFailWhenInsufficientStockForIncrease() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 10);
        var order = testDataHelper.createDefaultOrder();
        var orderItem =
                testDataHelper.createOrderItem(
                        order.getId(), product.getId(), 5, product.getPrice());

        // Manually reserve initial stock to simulate existing order item
        product.reserveStock(5);

        // Try to increase quantity to 12 when only 5 stock remains (10 - 5 = 5)
        // Need 7 more but only 5 available
        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new UpdateOrderItemCommand(
                                                order.getId(), orderItem.getId(), 12)))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void shouldFailWhenOrderItemDoesNotBelongToOrder() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order1 = testDataHelper.createDefaultOrder();
        var order2 = testDataHelper.createDefaultOrder();
        var orderItem =
                testDataHelper.createOrderItem(
                        order1.getId(), product.getId(), 5, product.getPrice());

        // Try to update orderItem with wrong orderId
        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new UpdateOrderItemCommand(
                                                order2.getId(), orderItem.getId(), 10)))
                .isInstanceOf(OrderItemNotBelongsToOrderException.class);
    }
}
