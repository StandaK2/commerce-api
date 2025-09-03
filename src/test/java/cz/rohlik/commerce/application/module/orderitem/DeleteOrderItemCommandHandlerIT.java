package cz.rohlik.commerce.application.module.orderitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.orderitem.command.DeleteOrderItemCommand;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotBelongsToOrderException;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DeleteOrderItemCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldDeleteOrderItem() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order = testDataHelper.createDefaultOrder();
        var orderItem =
                testDataHelper.createOrderItem(
                        order.getId(), product.getId(), 5, product.getPrice());

        // Manually reserve stock to simulate what command handler does
        product.reserveStock(5);
        var initialOrderUpdatedAt = order.getUpdatedAt();

        assertThat(orderItemRepository.count()).isEqualTo(1);

        var result =
                commandBus.execute(new DeleteOrderItemCommand(order.getId(), orderItem.getId()));

        assertThat(result.id()).isEqualTo(orderItem.getId());

        // Verify order item was hard deleted
        assertThat(orderItemRepository.count()).isZero();

        // Verify stock was released back to product (20 + 5 = 25)
        var updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(25);

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
                                        new DeleteOrderItemCommand(
                                                TestUtils.createUuidFromNumber(1),
                                                nonExistentOrderItemId)))
                .isInstanceOf(OrderItemNotFoundException.class);
    }

    @Test
    void shouldFailWhenOrderItemDoesNotBelongToOrder() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order1 = testDataHelper.createDefaultOrder();
        var order2 = testDataHelper.createDefaultOrder();
        var orderItem =
                testDataHelper.createOrderItem(
                        order1.getId(), product.getId(), 5, product.getPrice());

        // Try to delete orderItem with wrong orderId
        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new DeleteOrderItemCommand(
                                                order2.getId(), orderItem.getId())))
                .isInstanceOf(OrderItemNotBelongsToOrderException.class);
    }
}
