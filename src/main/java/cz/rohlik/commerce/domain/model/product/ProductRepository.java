package cz.rohlik.commerce.domain.model.product;

import cz.rohlik.commerce.domain.common.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product entity operations. Follows realtrek domain repository patterns.
 */
@Repository
public interface ProductRepository extends BaseRepository<Product> {}
