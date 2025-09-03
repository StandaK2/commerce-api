package cz.rohlik.commerce.domain.model.product;

import cz.rohlik.commerce.domain.common.BaseRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity operations. Follows realtrek domain repository patterns.
 */
@Repository
public interface ProductRepository extends BaseRepository<Product> {

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
