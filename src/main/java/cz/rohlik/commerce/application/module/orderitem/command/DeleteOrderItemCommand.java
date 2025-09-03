package cz.rohlik.commerce.application.module.orderitem.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeleteOrderItemCommand(
        @NotNull(message = "Order ID cannot be null") UUID orderId,
        @NotNull(message = "Order item ID cannot be null") UUID orderItemId)
        implements Command<IdResult> {}
