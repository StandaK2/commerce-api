package cz.rohlik.commerce.adapter.input.rest;

import cz.rohlik.commerce.adapter.input.rest.dto.CreateOrderItemRequest;
import cz.rohlik.commerce.adapter.input.rest.dto.UpdateOrderItemRequest;
import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.orderitem.command.DeleteOrderItemCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for order item management operations. Handles HTTP requests and delegates to
 * application layer through command bus.
 */
@Tag(name = "Order Items")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderItemController {

    private final CommandBus commandBus;

    @PostMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResult createOrderItem(
            @PathVariable UUID orderId, @RequestBody CreateOrderItemRequest request) {
        return commandBus.execute(request.toCommand(orderId));
    }

    @PutMapping("/{orderId}/items/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId,
            @RequestBody UpdateOrderItemRequest request) {
        commandBus.execute(request.toCommand(orderId, orderItemId));
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderItem(@PathVariable UUID orderId, @PathVariable UUID orderItemId) {
        commandBus.execute(new DeleteOrderItemCommand(orderId, orderItemId));
    }
}
