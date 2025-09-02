package cz.rohlik.commerce.domain.model.product;

import static org.junit.jupiter.api.Assertions.*;

import cz.rohlik.commerce.domain.model.product.exception.InsufficientStockException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void shouldCreateProductWithCorrectValues() {
        var product = new Product("Test Product", new BigDecimal("10.50"), 100);

        assertEquals("Test Product", product.getName());
        assertEquals(new BigDecimal("10.50"), product.getPrice());
        assertEquals(100, product.getStockQuantity());
    }

    @Test
    void shouldUpdatePropertiesCorrectly() {
        var product = new Product("Old Name", new BigDecimal("5.00"), 50);

        product.updateProperties("New Name", new BigDecimal("15.00"), 70);

        assertEquals("New Name", product.getName());
        assertEquals(new BigDecimal("15.00"), product.getPrice());
        assertEquals(70, product.getStockQuantity());
    }

    @Test
    void shouldReserveStockWhenAvailable() {
        var product = new Product("Test Product", new BigDecimal("10.00"), 100);

        product.reserveStock(30);

        assertEquals(70, product.getStockQuantity());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        var product = new Product("Test Product", new BigDecimal("10.00"), 10);

        var exception =
                assertThrows(InsufficientStockException.class, () -> product.reserveStock(15));

        assertTrue(exception.getMessage().contains("Test Product"));
        assertEquals(10, product.getStockQuantity()); // Should remain unchanged
    }

    @Test
    void shouldReleaseStock() {
        var product = new Product("Test Product", new BigDecimal("10.00"), 50);

        product.releaseStock(25);

        assertEquals(75, product.getStockQuantity());
    }
}
