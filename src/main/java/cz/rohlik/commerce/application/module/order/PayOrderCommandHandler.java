package cz.rohlik.commerce.application.module.order;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.command.PayOrderCommand;
import cz.rohlik.commerce.application.module.order.exception.PaymentNotProcessedException;
import cz.rohlik.commerce.application.module.order.port.output.PayOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command handler for processing order payments. Delegates to payment gateway and updates order
 * status based on payment result.
 */
@Service
@RequiredArgsConstructor
public class PayOrderCommandHandler implements CommandHandler<IdResult, PayOrderCommand> {

    private final OrderFinderService orderFinderService;
    private final PayOrder payOrder;

    @Override
    public Class<PayOrderCommand> getCommandClass() {
        return PayOrderCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(PayOrderCommand command) {
        var order = orderFinderService.findById(command.orderId());
        order.validateIfModifiable();

        var paymentId = payOrder.invoke(command.orderId());

        if (paymentId != null) {
            order.markAsPaid();
        } else {
            throw new PaymentNotProcessedException(command.orderId());
        }

        return IdResult.of(order.getId());
    }
}
