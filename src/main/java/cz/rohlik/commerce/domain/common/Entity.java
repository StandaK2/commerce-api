package cz.rohlik.commerce.domain.common;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

/** Base entity class with UUID for most domain entities. */
@MappedSuperclass
public abstract class Entity {
    @Id
    @Column(updatable = false)
    private UUID id;

    protected Entity() {
        this.id = UUID.randomUUID();
    }

    protected Entity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity entity = (Entity) obj;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
