package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.product.command.CreateProductCommand;
import cz.rohlik.commerce.domain.model.product.ProductCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Command handler for creating new products. Orchestrates product creation by delegating to domain
 * service.
 */
@Service
@RequiredArgsConstructor
public class CreateProductCommandHandler implements CommandHandler<IdResult, CreateProductCommand> {

    private final ProductCreateService productCreateService;

    @Override
    public Class<CreateProductCommand> getCommandClass() {
        return CreateProductCommand.class;
    }

    @Override
    public IdResult handle(CreateProductCommand command) {
        var product =
                productCreateService.create(
                        command.name(), command.price(), command.stockQuantity());

        return IdResult.of(product.getId());
    }
}
