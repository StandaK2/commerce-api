package cz.rohlik.commerce.application.module.orderitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.orderitem.command.CreateOrderItemCommand;
import cz.rohlik.commerce.domain.model.order.exception.OrderNotFoundException;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemAlreadyExistsException;
import cz.rohlik.commerce.domain.model.product.exception.InsufficientStockException;
import cz.rohlik.commerce.domain.model.product.exception.ProductNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CreateOrderItemCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldCreateOrderItem() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order = testDataHelper.createDefaultOrder();
        var initialOrderUpdatedAt = order.getUpdatedAt();

        var result =
                commandBus.execute(new CreateOrderItemCommand(order.getId(), product.getId(), 5));

        assertThat(result.id()).isNotNull();

        var orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(1);

        var orderItem = orderItems.get(0);
        assertThat(orderItem.getId()).isEqualTo(result.id());
        assertThat(orderItem.getOrderId()).isEqualTo(order.getId());
        assertThat(orderItem.getProductId()).isEqualTo(product.getId());
        assertThat(orderItem.getQuantity()).isEqualTo(5);
        assertThat(orderItem.getUnitPrice()).isEqualTo(new BigDecimal("79.99"));
        assertThat(orderItem.getSubtotal()).isEqualTo(new BigDecimal("399.95"));

        // Verify product stock was reserved
        var updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(20);

        // Verify order timestamp was updated
        var updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getUpdatedAt()).isAfter(initialOrderUpdatedAt);
    }

    @Test
    void shouldFailWhenOrderNotFound() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var nonExistentOrderId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new CreateOrderItemCommand(
                                                nonExistentOrderId, product.getId(), 5)))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldFailWhenProductNotFound() {
        var order = testDataHelper.createDefaultOrder();
        var nonExistentProductId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new CreateOrderItemCommand(
                                                order.getId(), nonExistentProductId, 5)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldFailWhenInsufficientStock() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 3);
        var order = testDataHelper.createDefaultOrder();

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new CreateOrderItemCommand(
                                                order.getId(), product.getId(), 5)))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void shouldFailWhenOrderItemAlreadyExists() {
        var product = testDataHelper.createProduct("Gaming Mouse", new BigDecimal("79.99"), 25);
        var order = testDataHelper.createDefaultOrder();

        commandBus.execute(new CreateOrderItemCommand(order.getId(), product.getId(), 3));

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new CreateOrderItemCommand(
                                                order.getId(), product.getId(), 2)))
                .isInstanceOf(OrderItemAlreadyExistsException.class);
    }
}
