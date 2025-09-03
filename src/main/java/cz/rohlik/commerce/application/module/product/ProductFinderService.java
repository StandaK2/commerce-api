package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.domain.model.product.Product;
import cz.rohlik.commerce.domain.model.product.ProductRepository;
import cz.rohlik.commerce.domain.model.product.exception.ProductNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductFinderService {

    private final ProductRepository productRepository;

    public Product findById(UUID productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public boolean hasActiveOrders(UUID productId) {
        return productRepository.hasActiveOrders(productId);
    }
}
