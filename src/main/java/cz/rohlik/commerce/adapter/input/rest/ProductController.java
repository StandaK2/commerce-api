package cz.rohlik.commerce.adapter.input.rest;

import cz.rohlik.commerce.adapter.input.rest.dto.CreateProductRequest;
import cz.rohlik.commerce.adapter.input.rest.dto.UpdateProductRequest;
import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.command.IdResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
