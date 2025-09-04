package cz.rohlik.commerce.application.module.orderitem;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.OrderFinderService;
import cz.rohlik.commerce.application.module.orderitem.command.CreateOrderItemCommand;
import cz.rohlik.commerce.application.module.product.ProductFinderService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemCreateService;
import cz.rohlik.commerce.domain.model.orderitem.OrderItemRepository;
import cz.rohlik.commerce.domain.model.orderitem.exception.OrderItemAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for creating new order items. Validates order existence, fetches product for
 * price and stock management, then creates the order item. Throws an exception if an order item for
 * the same product already exists in the order.
 */
@Service
@RequiredArgsConstructor
public class CreateOrderItemCommandHandler
        implements CommandHandler<IdResult, CreateOrderItemCommand> {

    private final OrderFinderService orderFinderService;
    private final ProductFinderService productFinderService;
    private final OrderItemCreateService orderItemCreateService;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Class<CreateOrderItemCommand> getCommandClass() {
        return CreateOrderItemCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(CreateOrderItemCommand command) {
        var order = orderFinderService.findById(command.orderId());
        order.validateIfModifiable();

        var existingOrderItem =
                orderItemRepository.findByOrderIdAndProductId(
                        command.orderId(), command.productId());

        if (existingOrderItem.isPresent()) {
            throw new OrderItemAlreadyExistsException(command.orderId(), command.productId());
        }

        var product = productFinderService.findByIdForStockOperation(command.productId());
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
