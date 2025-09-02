package cz.rohlik.commerce.application.module.product.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommand(
        @NotNull UUID productId,
        @NotBlank String name,
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal price,
        @NotNull @Min(0) Integer stockQuantity)
        implements Command<IdResult> {}
