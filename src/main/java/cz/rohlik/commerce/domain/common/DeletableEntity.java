package cz.rohlik.commerce.domain.common;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Base entity class that supports soft deletion.

 */
@MappedSuperclass
public abstract class DeletableEntity extends UpdatableEntity {
    private Instant deletedAt;

    protected DeletableEntity() {
        super();
    }

    protected DeletableEntity(UUID id) {
        super(id);
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted() {
        if (deletedAt == null) {
            deletedAt = Instant.now();
        }
    }

    public void reactivate() {
        deletedAt = null;
    }
}
