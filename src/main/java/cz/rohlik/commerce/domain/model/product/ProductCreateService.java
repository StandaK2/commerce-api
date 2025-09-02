package cz.rohlik.commerce.domain.model.product;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Domain service for creating Product entities. Handles simple Product creation logic. */
@Service
@RequiredArgsConstructor
public class ProductCreateService {

    private final ProductRepository productRepository;

    @Transactional
    public Product create(String name, BigDecimal price, Integer stockQuantity) {
        var product = new Product(name, price, stockQuantity);
        return productRepository.save(product);
    }
}
