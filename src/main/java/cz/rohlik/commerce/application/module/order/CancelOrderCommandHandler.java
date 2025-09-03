package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.command.CancelOrderCommand;
import cz.rohlik.commerce.application.module.orderitem.OrderItemFinderService;
import cz.rohlik.commerce.application.module.orderitem.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for canceling orders. Releases stock for all order items and marks order as
 * cancelled.
 */
@Service
@RequiredArgsConstructor
public class CancelOrderCommandHandler implements CommandHandler<IdResult, CancelOrderCommand> {

    private final OrderFinderService orderFinderService;
    private final OrderItemFinderService orderItemFinderService;
    private final OrderItemService orderItemService;

    @Override
    public Class<CancelOrderCommand> getCommandClass() {
        return CancelOrderCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(CancelOrderCommand command) {
        var order = orderFinderService.findById(command.orderId());

        var orderItems = orderItemFinderService.findAllByOrderId(command.orderId());
        orderItemService.releaseStockForOrderItems(orderItems);
        order.cancel();

        return IdResult.of(order.getId());
    }
}
