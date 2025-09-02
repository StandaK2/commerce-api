package cz.rohlik.commerce.domain.common;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;

/** Base entity class that supports soft deletion. */
@Getter
@MappedSuperclass
public abstract class DeletableEntity extends UpdatableEntity {
    @Column(name = "deleted_at")
    private Instant deletedAt;

    protected DeletableEntity() {
        super();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void markDeleted() {
        if (deletedAt == null) {
            deletedAt = Instant.now();
        }
    }
}
