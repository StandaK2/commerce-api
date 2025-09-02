package cz.rohlik.commerce;

import cz.rohlik.commerce.domain.model.order.Order;
import cz.rohlik.commerce.domain.model.order.OrderCreateService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItem;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemCreateService;
import cz.rohlik.commerce.domain.model.product.Product;
import cz.rohlik.commerce.domain.model.product.ProductCreateService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Helper class for creating test data in integration tests. */
@Component
@Transactional
@RequiredArgsConstructor
public class IntegrationTestDataHelper {

    private final ProductCreateService productCreateService;
    private final OrderCreateService orderCreateService;
    private final OrderItemCreateService orderItemCreateService;

    public Product createDefaultProduct() {
        return productCreateService.create("Test Product", new BigDecimal("19.99"), 100);
    }

    public Product createProduct(String name, BigDecimal price, Integer stockQuantity) {
        return productCreateService.create(name, price, stockQuantity);
    }

    public Order createDefaultOrder() {
        return orderCreateService.create();
    }

    public OrderItem createDefaultOrderItem(UUID orderId, UUID productId) {
        return orderItemCreateService.create(orderId, productId, 2, new BigDecimal("15.00"));
    }

    public OrderItem createOrderItem(
            UUID orderId, UUID productId, Integer quantity, BigDecimal unitPrice) {
        return orderItemCreateService.create(orderId, productId, quantity, unitPrice);
    }
}
