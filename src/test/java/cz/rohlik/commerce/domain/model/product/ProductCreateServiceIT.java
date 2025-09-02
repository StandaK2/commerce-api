package cz.rohlik.commerce.domain.model.product;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.IntegrationTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductCreateServiceIT extends IntegrationTest {

    @Test
    void shouldCreateProductSuccessfully() {
        assertEquals(0, productRepository.count());

        var product = productCreateService.create("Test Product", new BigDecimal("25.99"), 150);

        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(new BigDecimal("25.99"), product.getPrice());
        assertEquals(150, product.getStockQuantity());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());

        assertEquals(1, productRepository.count());

        var savedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(savedProduct);
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getStockQuantity(), savedProduct.getStockQuantity());
    }
}
