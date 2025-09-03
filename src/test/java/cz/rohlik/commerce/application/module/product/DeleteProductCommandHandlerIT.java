package cz.rohlik.commerce.application.module.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.module.product.command.DeleteProductCommand;
import cz.rohlik.commerce.domain.model.product.exception.ProductHasActiveOrdersException;
import cz.rohlik.commerce.domain.model.product.exception.ProductNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DeleteProductCommandHandlerIT extends IntegrationTest {

    @Test
    void shouldSoftDeleteProductWhenNoActiveOrders() {
        var product =
                testDataHelper.createProduct("Product to Delete", new BigDecimal("99.99"), 50);

        var result = commandBus.execute(new DeleteProductCommand(product.getId()));

        assertThat(result.id()).isEqualTo(product.getId());
        var deletedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(deletedProduct.getDeletedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenProductHasActiveOrders() {
        var product =
                testDataHelper.createProduct("Product with Orders", new BigDecimal("99.99"), 50);
        var order = testDataHelper.createDefaultOrder();
        testDataHelper.createOrderItem(order.getId(), product.getId(), 2, new BigDecimal("99.99"));

        assertThatThrownBy(() -> commandBus.execute(new DeleteProductCommand(product.getId())))
                .isInstanceOf(ProductHasActiveOrdersException.class)
                .hasMessageContaining(product.getId().toString());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var nonExistentId = TestUtils.createUuidFromNumber(999);

        assertThatThrownBy(() -> commandBus.execute(new DeleteProductCommand(nonExistentId)))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
