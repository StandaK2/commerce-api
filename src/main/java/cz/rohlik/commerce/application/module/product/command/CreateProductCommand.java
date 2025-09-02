package cz.rohlik.commerce.application.module.product.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateProductCommand(
        @NotBlank(message = "Product name cannot be blank") String name,
        @NotNull(message = "Price cannot be null")
                @DecimalMin(value = "0.01", message = "Price must be greater than zero")
                BigDecimal price,
        @NotNull(message = "Stock quantity cannot be null")
                @Min(value = 0, message = "Stock quantity cannot be negative")
                Integer stockQuantity)
        implements Command<IdResult> {}
