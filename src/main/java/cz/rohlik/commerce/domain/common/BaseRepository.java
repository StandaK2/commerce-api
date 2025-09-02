package cz.rohlik.commerce.domain.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Base repository interface extending JpaRepository.
 * Keep simple - add methods only when actually needed.
 *
 * @param <E> Entity type that extends Entity
 */
@NoRepositoryBean
public interface BaseRepository<E extends Entity> extends JpaRepository<E, UUID> {
    // Keep minimal - add methods only when we need them
}
