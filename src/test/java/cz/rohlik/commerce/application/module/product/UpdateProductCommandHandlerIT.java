package cz.rohlik.commerce.application.module.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.product.command.UpdateProductCommand;
import cz.rohlik.commerce.domain.model.product.exception.ProductNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class UpdateProductCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldUpdateProduct() {
        var product = testDataHelper.createProduct("Original Product", new BigDecimal("99.99"), 50);

        var result =
                commandBus.execute(
                        new UpdateProductCommand(
                                product.getId(), "Updated Product", new BigDecimal("149.99"), 75));

        assertThat(result.id()).isEqualTo(product.getId());

        var updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getName()).isEqualTo("Updated Product");
        assertThat(updatedProduct.getPrice()).isEqualTo(new BigDecimal("149.99"));
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(75);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var nonExistentId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(
                        () ->
                                commandBus.execute(
                                        new UpdateProductCommand(
                                                nonExistentId,
                                                "Updated Product",
                                                new BigDecimal("149.99"),
                                                75)))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
