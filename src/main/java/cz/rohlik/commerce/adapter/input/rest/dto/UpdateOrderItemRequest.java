package cz.rohlik.commerce.adapter.input.rest.dto;

import cz.rohlik.commerce.application.module.orderitem.command.UpdateOrderItemCommand;
import java.util.UUID;

public record UpdateOrderItemRequest(Integer quantity) {

    public UpdateOrderItemCommand toCommand(UUID orderId, UUID orderItemId) {
        return new UpdateOrderItemCommand(orderId, orderItemId, quantity);
    }
}
