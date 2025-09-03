package cz.rohlik.commerce;

import cz.rohlik.commerce.domain.model.order.Order;
import cz.rohlik.commerce.domain.model.order.OrderRepository;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import cz.rohlik.commerce.domain.model.orderitem.OrderItem;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemRepository;
import cz.rohlik.commerce.domain.model.product.Product;
import cz.rohlik.commerce.domain.model.product.ProductRepository;
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

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Product createDefaultProduct() {
        return createProduct("Test Product", new BigDecimal("19.99"), 100);
    }

    public Product createProduct(String name, BigDecimal price, Integer stockQuantity) {
        var product = new Product(name, price, stockQuantity);
        return productRepository.save(product);
    }

    public Product createDeletedProduct(String name, BigDecimal price, Integer stockQuantity) {
        var product = new Product(name, price, stockQuantity);
        product.markDeleted();
        return productRepository.save(product);
    }

    public Order createDefaultOrder() {
        return createOrder(OrderStatus.CREATED);
    }

    public Order createOrder(OrderStatus status) {
        var order = new Order();
        order = orderRepository.save(order); // Save first to get ID

        // Apply status changes if needed
        switch (status) {
            case PAID -> {
                order.markAsPaid();
                order = orderRepository.save(order);
            }
            case CANCELLED -> {
                order.cancel();
                order = orderRepository.save(order);
            }
            case EXPIRED -> {
                order.markAsExpired();
                order = orderRepository.save(order);
            }
            case CREATED -> {} // Default status, no additional action needed
        }
        return order;
    }

    public OrderItem createDefaultOrderItem(UUID orderId, UUID productId) {
        return createOrderItem(orderId, productId, 2, new BigDecimal("15.00"));
    }

    public OrderItem createOrderItem(
            UUID orderId, UUID productId, Integer quantity, BigDecimal unitPrice) {
        var orderItem = new OrderItem(orderId, productId, quantity, unitPrice);
        return orderItemRepository.save(orderItem);
    }
}
