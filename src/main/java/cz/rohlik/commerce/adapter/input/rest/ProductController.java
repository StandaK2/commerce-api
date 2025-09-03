package cz.rohlik.commerce.adapter.input.rest;

import cz.rohlik.commerce.adapter.input.rest.dto.CreateProductRequest;
import cz.rohlik.commerce.adapter.input.rest.dto.UpdateProductRequest;
import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.common.query.QueryBus;
import cz.rohlik.commerce.application.module.product.command.DeleteProductCommand;
import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for product management operations. Handles HTTP requests and delegates to
 * application layer through command/query bus.
 */
@Tag(name = "Products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @GetMapping
    public List<GetProductsQuery.Result> getProducts() {
        return queryBus.execute(new GetProductsQuery());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResult createProduct(@RequestBody CreateProductRequest request) {
        return commandBus.execute(request.toCommand());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable UUID id, @RequestBody UpdateProductRequest request) {
        commandBus.execute(request.toCommand(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        commandBus.execute(new DeleteProductCommand(id));
    }
}
