package cz.rohlik.commerce.domain.common;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

/**
 * Base entity class that tracks creation and update information. Simplified for commerce API - no
 * JPA auditing to keep it simple.
 */
@Getter
@MappedSuperclass
public abstract class UpdatableEntity extends CreatableEntity {
    @Version
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UpdatableEntity() {
        super();
        this.updatedAt = getCreatedAt();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
        doOnUpdate();
    }

    /**
     * Hook method called before entity update. Override in subclasses to add custom pre-update
     * logic.
     */
    protected void doOnUpdate() {
        // Default implementation does nothing
    }
}
