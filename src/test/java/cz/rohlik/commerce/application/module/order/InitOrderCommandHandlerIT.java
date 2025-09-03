package cz.rohlik.commerce.application.module.order;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.application.module.order.command.InitOrderCommand;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import org.junit.jupiter.api.Test;

class InitOrderCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldCreateNewOrder() {
        var result = commandBus.execute(new InitOrderCommand());

        assertThat(result.id()).isNotNull();

        var createdOrder = orderRepository.findById(result.id()).orElseThrow();
        assertThat(createdOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(createdOrder.getCreatedAt()).isNotNull();
        assertThat(createdOrder.getUpdatedAt()).isNotNull();
    }
}
