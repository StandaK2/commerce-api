package cz.rohlik.commerce.domain.model.orderitem;

import cz.rohlik.commerce.domain.common.BaseRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/** Repository interface for OrderItem entity operations. */
@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem> {

    List<OrderItem> findAllByOrderId(UUID orderId);

    Optional<OrderItem> findByOrderIdAndProductId(UUID orderId, UUID productId);
}
