package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.command.InitOrderCommand;
import cz.rohlik.commerce.domain.model.order.OrderCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Command handler for initializing new orders. Creates a new order with default status. */
@Service
@RequiredArgsConstructor
public class InitOrderCommandHandler implements CommandHandler<IdResult, InitOrderCommand> {

    private final OrderCreateService orderCreateService;

    @Override
    public Class<InitOrderCommand> getCommandClass() {
        return InitOrderCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(InitOrderCommand command) {
        var order = orderCreateService.create();
        return IdResult.of(order.getId());
    }
}
