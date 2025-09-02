package cz.rohlik.commerce.domain.common;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

/**
 * Base entity class that tracks creation information. Simplified for commerce API - no JPA auditing
 * to keep it simple.
 */
@Getter
@MappedSuperclass
public abstract class CreatableEntity extends Entity {
    @Column(name = "created_at", updatable = false, nullable = false)
    private final Instant createdAt;

    protected CreatableEntity() {
        super();
        this.createdAt = Instant.now();
    }

    @PrePersist
    private void onPersist() {
        doOnPersist();
    }

    /**
     * Hook method called before entity persistence. Override in subclasses to add custom
     * pre-persist logic.
     */
    protected void doOnPersist() {
        // Default implementation does nothing
    }
}
