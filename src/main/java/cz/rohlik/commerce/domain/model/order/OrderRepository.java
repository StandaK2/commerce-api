package cz.rohlik.commerce.domain.model.order;

import cz.rohlik.commerce.domain.common.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Order entity operations. Follows realtrek domain repository patterns.
 */
@Repository
public interface OrderRepository extends BaseRepository<Order> {}
