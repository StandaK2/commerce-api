package cz.rohlik.commerce.adapter.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cz.rohlik.commerce.ControllerTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.command.CancelOrderCommand;
import cz.rohlik.commerce.application.module.order.command.InitOrderCommand;
import cz.rohlik.commerce.application.module.order.command.PayOrderCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(OrderController.class)
class OrderControllerTest extends ControllerTest {

    @Test
    void shouldCorrectlyAccessInitOrderEP() throws Exception {
        var expectedOrderId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(InitOrderCommand.class)))
                .thenReturn(IdResult.of(expectedOrderId));

        mockMvc.perform(post("/api/orders/init"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expectedOrderId.toString()));

        verify(commandBus).execute(any(InitOrderCommand.class));
    }

    @Test
    void shouldCorrectlyAccessCancelOrderEP() throws Exception {
        var orderId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(CancelOrderCommand.class))).thenReturn(IdResult.of(orderId));

        mockMvc.perform(post("/api/orders/" + orderId + "/cancel"))
                .andExpect(status().isNoContent());

        verify(commandBus).execute(new CancelOrderCommand(orderId));
    }

    @Test
    void shouldCorrectlyAccessPayOrderEP() throws Exception {
        var orderId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(PayOrderCommand.class))).thenReturn(IdResult.of(orderId));

        mockMvc.perform(post("/api/orders/" + orderId + "/pay")).andExpect(status().isNoContent());

        verify(commandBus).execute(new PayOrderCommand(orderId));
    }
}
