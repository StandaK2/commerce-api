package cz.rohlik.commerce.application.module.product;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.application.module.product.command.CreateProductCommand;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CreateProductCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldCreateProduct() {
        assertThat(productRepository.count()).isZero();

        var result =
                commandBus.execute(
                        new CreateProductCommand("Gaming Mouse", new BigDecimal("79.99"), 25));

        assertThat(result.id()).isNotNull();

        var products = productRepository.findAll();
        assertThat(products).hasSize(1);

        var product = products.get(0);
        assertThat(product.getId()).isEqualTo(result.id());
        assertThat(product.getName()).isEqualTo("Gaming Mouse");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("79.99"));
        assertThat(product.getStockQuantity()).isEqualTo(25);
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldCreateProductWithMinimalData() {
        var result =
                commandBus.execute(
                        new CreateProductCommand("Basic Product", new BigDecimal("0.01"), 0));

        assertThat(result.id()).isNotNull();

        var product = productRepository.findById(result.id()).orElseThrow();
        assertThat(product.getName()).isEqualTo("Basic Product");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("0.01"));
        assertThat(product.getStockQuantity()).isZero();
    }
}
