package cz.rohlik.commerce.application.module.orderitem.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateOrderItemCommand(
        @NotNull(message = "Order ID cannot be null") UUID orderId,
        @NotNull(message = "Order item ID cannot be null") UUID orderItemId,
        @NotNull(message = "Quantity cannot be null")
                @Min(value = 1, message = "Quantity must be at least 1")
                Integer quantity)
        implements Command<IdResult> {}
