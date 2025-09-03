package cz.rohlik.commerce.application.module.orderitem.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderItemCommand(
        @NotNull(message = "Order ID cannot be null") UUID orderId,
        @NotNull(message = "Product ID cannot be null") UUID productId,
        @NotNull(message = "Quantity cannot be null")
                @Min(value = 1, message = "Quantity must be at least 1")
                Integer quantity)
        implements Command<IdResult> {}
