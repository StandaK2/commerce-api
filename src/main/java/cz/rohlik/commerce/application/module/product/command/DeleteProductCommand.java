package cz.rohlik.commerce.application.module.product.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeleteProductCommand(@NotNull UUID productId) implements Command<IdResult> {}
