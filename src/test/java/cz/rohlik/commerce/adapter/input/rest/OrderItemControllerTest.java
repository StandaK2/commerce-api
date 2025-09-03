package cz.rohlik.commerce.adapter.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cz.rohlik.commerce.ControllerTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.orderitem.command.CreateOrderItemCommand;
import cz.rohlik.commerce.application.module.orderitem.command.DeleteOrderItemCommand;
import cz.rohlik.commerce.application.module.orderitem.command.UpdateOrderItemCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest extends ControllerTest {

    @Test
    void shouldCorrectlyAccessCreateOrderItemEP() throws Exception {
        var expectedOrderItemId = TestUtils.createUuidFromNumber(1);
        var orderId = TestUtils.createUuidFromNumber(2);
        var productId = TestUtils.createUuidFromNumber(3);

        when(commandBus.execute(any(CreateOrderItemCommand.class)))
                .thenReturn(IdResult.of(expectedOrderItemId));

        mockMvc.perform(
                        TestUtils.addJsonContent(
                                post("/api/orders/{orderId}/items", orderId),
                                """
                        {
                            "productId": "%s",
                            "quantity": 5
                        }
                        """
                                        .formatted(productId)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expectedOrderItemId.toString()));

        verify(commandBus).execute(new CreateOrderItemCommand(orderId, productId, 5));
    }

    @Test
    void shouldCorrectlyAccessUpdateOrderItemEP() throws Exception {
        var orderId = TestUtils.createUuidFromNumber(2);
        var orderItemId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(UpdateOrderItemCommand.class)))
                .thenReturn(IdResult.of(orderItemId));

        mockMvc.perform(
                        TestUtils.addJsonContent(
                                put(
                                        "/api/orders/{orderId}/items/{orderItemId}",
                                        orderId,
                                        orderItemId),
                                """
                        {
                            "quantity": 10
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(commandBus).execute(new UpdateOrderItemCommand(orderId, orderItemId, 10));
    }

    @Test
    void shouldCorrectlyAccessDeleteOrderItemEP() throws Exception {
        var orderId = TestUtils.createUuidFromNumber(2);
        var orderItemId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(DeleteOrderItemCommand.class)))
                .thenReturn(IdResult.of(orderItemId));

        mockMvc.perform(delete("/api/orders/{orderId}/items/{orderItemId}", orderId, orderItemId))
                .andExpect(status().isNoContent());

        verify(commandBus).execute(new DeleteOrderItemCommand(orderId, orderItemId));
    }
}
