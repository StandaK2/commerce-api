package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItem;
import cz.rohlik.commerce.domain.model.product.Product;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    private final ProductFinderService productFinderService;

    public void releaseStockForOrderItem(OrderItem orderItem) {
        var product = productFinderService.findByIdForStockOperation(orderItem.getProductId());
        product.releaseStock(orderItem.getQuantity());
    }

    public void releaseStockForOrderItems(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            return;
        }

        List<UUID> productIds =
                orderItems.stream().map(OrderItem::getProductId).distinct().toList();

        Map<UUID, Product> productsMap =
                productFinderService.findAllByIdInForStockOperations(productIds);

        for (var orderItem : orderItems) {
            var product = productsMap.get(orderItem.getProductId());
            product.releaseStock(orderItem.getQuantity());
        }
    }
}
