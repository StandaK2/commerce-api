package cz.rohlik.commerce.adapter.input.rest.dto;

import cz.rohlik.commerce.application.module.product.command.CreateProductCommand;
import java.math.BigDecimal;

public record CreateProductRequest(String name, BigDecimal price, Integer stockQuantity) {

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(name, price, stockQuantity);
    }
}
