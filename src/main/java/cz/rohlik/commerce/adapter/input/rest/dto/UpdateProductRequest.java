package cz.rohlik.commerce.adapter.input.rest.dto;

import cz.rohlik.commerce.application.module.product.command.UpdateProductCommand;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(String name, BigDecimal price, Integer stockQuantity) {

    public UpdateProductCommand toCommand(UUID productId) {
        return new UpdateProductCommand(productId, name, price, stockQuantity);
    }
}
