package cz.rohlik.commerce.domain.model.order;

import cz.rohlik.commerce.domain.common.BaseRepository;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

/** Repository interface for Order entity operations. */
@Repository
public interface OrderRepository extends BaseRepository<Order> {

    List<Order> findAllByStatusAndUpdatedAtBefore(OrderStatus status, Instant updatedAtBefore);
}
