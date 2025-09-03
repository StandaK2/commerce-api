package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.application.common.command.CommandHandler;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.product.command.DeleteProductCommand;
import cz.rohlik.commerce.domain.model.product.exception.ProductHasActiveOrdersException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProductCommandHandler implements CommandHandler<IdResult, DeleteProductCommand> {

    private final ProductFinderService productFinderService;

    @Override
    public Class<DeleteProductCommand> getCommandClass() {
        return DeleteProductCommand.class;
    }

    @Override
    @Transactional
    public IdResult handle(DeleteProductCommand command) {
        if (productFinderService.hasActiveOrders(command.productId())) {
            throw new ProductHasActiveOrdersException(command.productId());
        }

        var product = productFinderService.findById(command.productId());
        product.markDeleted();

        return IdResult.of(product.getId());
    }
}
