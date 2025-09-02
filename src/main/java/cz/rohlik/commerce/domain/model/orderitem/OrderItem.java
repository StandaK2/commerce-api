package cz.rohlik.commerce.domain.model.orderitem;

import cz.rohlik.commerce.domain.common.CreatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OrderItem entity representing a product item within an order. Contains product reference,
 * quantity, and unit price at the time of order.
 */
@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends CreatableEntity {

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
