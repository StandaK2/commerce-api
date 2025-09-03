package cz.rohlik.commerce.domain.model.order;

import cz.rohlik.commerce.domain.common.UpdatableEntity;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.order.exception.InvalidOrderStateException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Order domain entity representing a customer's order. Contains business logic for order state
 * management. Order items are fetched separately via OrderItemRepository.
 */
@Entity
@Table(name = "\"order\"")
@Getter
@NoArgsConstructor
public class Order extends UpdatableEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    public void markAsPaid() {
        validateIfModifiable();
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        validateIfModifiable();
        this.status = OrderStatus.CANCELLED;
    }

    public void markAsExpired() {
        validateIfModifiable();
        this.status = OrderStatus.EXPIRED;
    }

    public boolean isExpired(int expirationTimeInMinutes) {
        var expirationTime = getUpdatedAt().plus(expirationTimeInMinutes, ChronoUnit.MINUTES);
        return Instant.now().isAfter(expirationTime);
    }

    public void validateIfModifiable() {
        if (status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    getId(), String.format("Invalid order status %s for this operation", status));
        }
    }
}
