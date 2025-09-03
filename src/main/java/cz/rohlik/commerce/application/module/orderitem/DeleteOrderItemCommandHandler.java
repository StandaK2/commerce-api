package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.OrderFinderService;
import cz.rohlik.commerce.application.module.orderitem.command.DeleteOrderItemCommand;
import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemNotBelongsToOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for deleting order items. Fetches order item, releases stock on associated
 * product, and performs hard delete of the order item.
 */
@Service
@RequiredArgsConstructor
public class DeleteOrderItemCommandHandler
        implements CommandHandler<IdResult, DeleteOrderItemCommand> {

    private final OrderItemFinderService orderItemFinderService;
    private final ProductFinderService productFinderService;
    private final OrderFinderService orderFinderService;

    @Override
    public Class<DeleteOrderItemCommand> getCommandClass() {
        return DeleteOrderItemCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(DeleteOrderItemCommand command) {
        var orderItem = orderItemFinderService.findById(command.orderItemId());

        if (!orderItem.getOrderId().equals(command.orderId())) {
            throw new OrderItemNotBelongsToOrderException(command.orderItemId(), command.orderId());
        }

        var product = productFinderService.findById(orderItem.getProductId());
        var order = orderFinderService.findById(orderItem.getOrderId());

        order.validateIfModifiable();
        product.releaseStock(orderItem.getQuantity());
        orderItemFinderService.deleteById(command.orderItemId());
        order.markAsModified();

        return IdResult.of(orderItem.getId());
    }
}
