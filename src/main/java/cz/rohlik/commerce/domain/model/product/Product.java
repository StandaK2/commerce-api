package cz.rohlik.commerce.domain.model.product;

import cz.rohlik.commerce.domain.common.DeletableEntity;
import cz.rohlik.commerce.domain.model.product.exception.InsufficientStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Product domain entity representing items available for purchase. Contains business logic for
 * stock management and validation.
 */
@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends DeletableEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    public void updateProperties(String name, BigDecimal price, Integer stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void reserveStock(Integer quantity) {
        if (!canFulfillOrder(quantity)) {
            throw new InsufficientStockException(getId(), name, stockQuantity, quantity);
        }
        this.stockQuantity -= quantity;
    }

    public void releaseStock(Integer quantity) {
        this.stockQuantity += quantity;
    }

    private boolean canFulfillOrder(Integer quantity) {
        return stockQuantity >= quantity;
    }
}
