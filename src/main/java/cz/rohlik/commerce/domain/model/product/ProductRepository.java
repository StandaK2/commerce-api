package cz.rohlik.commerce.domain.model.product;

import cz.rohlik.commerce.domain.common.BaseRepository;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository interface for Product entity operations. */
@Repository
public interface ProductRepository extends BaseRepository<Product> {

    List<Product> findAllByIdIn(List<UUID> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIdInForUpdate(@Param("ids") List<UUID> ids);

    @Query(
            value =
                    """
            SELECT EXISTS(
                SELECT 1
                FROM order_item oi
                INNER JOIN "order" o ON oi.order_id = o.id
                WHERE oi.product_id = :productId
                AND o.status = 'CREATED'
            )
            """,
            nativeQuery = true)
    boolean hasActiveOrders(@Param("productId") UUID productId);
}
