package cz.rohlik.commerce.application.module.order.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PayOrderCommand(@NotNull(message = "Order ID cannot be null") UUID orderId)
        implements Command<IdResult> {}
