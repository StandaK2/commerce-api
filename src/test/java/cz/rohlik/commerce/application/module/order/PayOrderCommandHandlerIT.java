package cz.rohlik.commerce.application.module.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.order.command.PayOrderCommand;
import cz.rohlik.commerce.application.module.order.exception.PaymentNotProcessedException;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.order.exception.InvalidOrderStateException;
import cz.rohlik.commerce.domain.model.order.exception.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PayOrderCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldPayOrderSuccessfully() {
        var order = testDataHelper.createDefaultOrder();
        when(payOrder.invoke(order.getId())).thenReturn("pi_3QK1234567890abcdef");

        var result = commandBus.execute(new PayOrderCommand(order.getId()));

        assertThat(result.id()).isEqualTo(order.getId());

        // Verify order status changed to PAID
        var paidOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(paidOrder.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldFailWhenPaymentNotProcessed() {
        var order = testDataHelper.createDefaultOrder();
        when(payOrder.invoke(order.getId())).thenReturn(null);

        assertThatThrownBy(() -> commandBus.execute(new PayOrderCommand(order.getId())))
                .isInstanceOf(PaymentNotProcessedException.class);

        // Verify order status remains CREATED
        var unchangedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(unchangedOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    void shouldFailWhenOrderNotFound() {
        var nonExistentOrderId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(() -> commandBus.execute(new PayOrderCommand(nonExistentOrderId)))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @ParameterizedTest
    @EnumSource(
            value = OrderStatus.class,
            names = {"PAID", "CANCELLED", "EXPIRED"})
    void shouldFailWhenOrderNotInCreatedStatus(OrderStatus invalidStatus) {
        var order = testDataHelper.createOrder(invalidStatus);

        assertThatThrownBy(() -> commandBus.execute(new PayOrderCommand(order.getId())))
                .isInstanceOf(InvalidOrderStateException.class);
    }
}
