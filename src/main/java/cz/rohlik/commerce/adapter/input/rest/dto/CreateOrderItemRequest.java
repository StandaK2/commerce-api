package cz.rohlik.commerce.adapter.input.rest.dto;

import cz.rohlik.commerce.application.module.orderitem.command.CreateOrderItemCommand;
import java.util.UUID;

public record CreateOrderItemRequest(UUID productId, Integer quantity) {

    public CreateOrderItemCommand toCommand(UUID orderId) {
        return new CreateOrderItemCommand(orderId, productId, quantity);
    }
}
