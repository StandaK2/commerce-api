package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.product.command.UpdateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProductCommandHandler implements CommandHandler<IdResult, UpdateProductCommand> {

    private final ProductFinderService productFinderService;

    @Override
    public Class<UpdateProductCommand> getCommandClass() {
        return UpdateProductCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(UpdateProductCommand command) {
        var product = productFinderService.findById(command.productId());

        product.updateProperties(command.name(), command.price(), command.stockQuantity());

        return IdResult.of(product.getId());
    }
}
