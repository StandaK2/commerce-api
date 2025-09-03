package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.OrderFinderService;
import cz.rohlik.commerce.application.module.orderitem.command.UpdateOrderItemCommand;
import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotBelongsToOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for updating order items. Fetches existing order item, manages stock changes
 * based on quantity differences, and updates the order item.
 */
@Service
@RequiredArgsConstructor
public class UpdateOrderItemCommandHandler
        implements CommandHandler<IdResult, UpdateOrderItemCommand> {

    private final OrderItemFinderService orderItemFinderService;
    private final ProductFinderService productFinderService;
    private final OrderFinderService orderFinderService;

    @Override
    public Class<UpdateOrderItemCommand> getCommandClass() {
        return UpdateOrderItemCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(UpdateOrderItemCommand command) {
        var orderItem = orderItemFinderService.findById(command.orderItemId());

        if (!orderItem.getOrderId().equals(command.orderId())) {
            throw new OrderItemNotBelongsToOrderException(command.orderItemId(), command.orderId());
        }

        var product = productFinderService.findById(orderItem.getProductId());
        var order = orderFinderService.findById(orderItem.getOrderId());
        order.validateIfModifiable();

        var currentQuantity = orderItem.getQuantity();
        var newQuantity = command.quantity();
        var quantityDifference = newQuantity - currentQuantity;

        if (quantityDifference > 0) {
            product.reserveStock(quantityDifference);
        } else if (quantityDifference < 0) {
            product.releaseStock(-quantityDifference);
        }

        orderItem.updateQuantity(newQuantity);
        order.markAsModified();

        return IdResult.of(orderItem.getId());
    }
}
