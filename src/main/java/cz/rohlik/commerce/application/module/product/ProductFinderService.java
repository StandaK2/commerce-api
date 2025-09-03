package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.domain.model.product.Product;
import cz.rohlik.commerce.domain.model.product.ProductRepository;
import cz.rohlik.commerce.domain.model.product.exception.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public Map<UUID, Product> findAllByIdInAsMap(List<UUID> productIds) {
        return productRepository.findAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    @Transactional
    public Product findByIdForStockOperation(UUID productId) {
        return productRepository
                .findByIdForUpdate(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public Map<UUID, Product> findAllByIdInForStockOperations(List<UUID> productIds) {
        return productRepository.findAllByIdInForUpdate(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}
