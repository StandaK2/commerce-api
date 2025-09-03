package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.OrderFinderService;
import cz.rohlik.commerce.application.module.orderitem.command.CreateOrderItemCommand;
import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for creating new order items. Validates order existence, fetches product for
 * price and stock management, then creates the order item.
 */
@Service
@RequiredArgsConstructor
public class CreateOrderItemCommandHandler
        implements CommandHandler<IdResult, CreateOrderItemCommand> {

    private final OrderFinderService orderFinderService;
    private final ProductFinderService productFinderService;
    private final OrderItemCreateService orderItemCreateService;

    @Override
    public Class<CreateOrderItemCommand> getCommandClass() {
        return CreateOrderItemCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(CreateOrderItemCommand command) {
        var order = orderFinderService.findById(command.orderId());
        order.validateIfModifiable();

        var product = productFinderService.findById(command.productId());
        product.reserveStock(command.quantity());

        var orderItem =
                orderItemCreateService.create(
                        command.orderId(),
                        command.productId(),
                        command.quantity(),
                        product.getPrice());

        order.markAsModified();

        return IdResult.of(orderItem.getId());
    }
}
