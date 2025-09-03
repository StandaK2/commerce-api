package cz.rohlik.commerce.adapter.input.rest;

import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.order.command.CancelOrderCommand;
import cz.rohlik.commerce.application.module.order.command.InitOrderCommand;
import cz.rohlik.commerce.application.module.order.command.PayOrderCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for order management operations. Handles HTTP requests and delegates to
 * application layer through command bus.
 */
@Tag(name = "Orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CommandBus commandBus;

    @PostMapping("/init")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResult initOrder() {
        return commandBus.execute(new InitOrderCommand());
    }

    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable UUID orderId) {
        commandBus.execute(new CancelOrderCommand(orderId));
    }

    @PostMapping("/{orderId}/pay")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void payOrder(@PathVariable UUID orderId) {
        commandBus.execute(new PayOrderCommand(orderId));
    }
}
