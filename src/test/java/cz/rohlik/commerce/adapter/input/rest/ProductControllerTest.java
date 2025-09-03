package cz.rohlik.commerce.adapter.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cz.rohlik.commerce.ControllerTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.product.command.CreateProductCommand;
import cz.rohlik.commerce.application.module.product.command.DeleteProductCommand;
import cz.rohlik.commerce.application.module.product.command.UpdateProductCommand;
import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(ProductController.class)
class ProductControllerTest extends ControllerTest {

    @Test
    void shouldCorrectlyAccessGetProductsEP() throws Exception {
        var product1Id = TestUtils.createUuidFromNumber(1);
        var product2Id = TestUtils.createUuidFromNumber(2);
        var now = Instant.now();

        var mockResults =
                List.of(
                        new GetProductsQuery.Result(
                                product1Id,
                                "Wireless Headphones",
                                new BigDecimal("99.99"),
                                50,
                                now,
                                10L,
                                new BigDecimal("999.90")),
                        new GetProductsQuery.Result(
                                product2Id,
                                "Gaming Mouse",
                                new BigDecimal("49.99"),
                                25,
                                now,
                                5L,
                                new BigDecimal("249.95")));

        when(queryBus.execute(any(GetProductsQuery.class))).thenReturn(mockResults);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(product1Id.toString()))
                .andExpect(jsonPath("$[0].name").value("Wireless Headphones"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[0].stockQuantity").value(50))
                .andExpect(jsonPath("$[0].soldCount").value(10))
                .andExpect(jsonPath("$[0].soldSum").value(999.90))
                .andExpect(jsonPath("$[1].id").value(product2Id.toString()))
                .andExpect(jsonPath("$[1].name").value("Gaming Mouse"))
                .andExpect(jsonPath("$[1].price").value(49.99))
                .andExpect(jsonPath("$[1].stockQuantity").value(25))
                .andExpect(jsonPath("$[1].soldCount").value(5))
                .andExpect(jsonPath("$[1].soldSum").value(249.95));

        verify(queryBus).execute(any(GetProductsQuery.class));
    }

    @Test
    void shouldCorrectlyAccessCreateProductEP() throws Exception {
        var expectedProductId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(CreateProductCommand.class)))
                .thenReturn(IdResult.of(expectedProductId));

        mockMvc.perform(
                        TestUtils.addJsonContent(
                                post("/api/products"),
                                """
                        {
                            "name": "Wireless Headphones",
                            "price": 99.99,
                            "stockQuantity": 50
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(expectedProductId.toString()));

        verify(commandBus)
                .execute(
                        new CreateProductCommand(
                                "Wireless Headphones", new BigDecimal("99.99"), 50));
    }

    @Test
    void shouldCorrectlyAccessUpdateProductEP() throws Exception {
        var productId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(UpdateProductCommand.class)))
                .thenReturn(IdResult.of(productId));

        mockMvc.perform(
                        TestUtils.addJsonContent(
                                put("/api/products/" + productId),
                                """
                        {
                            "name": "Updated Headphones",
                            "price": 129.99,
                            "stockQuantity": 75
                        }
                        """))
                .andExpect(status().isNoContent());

        verify(commandBus)
                .execute(
                        new UpdateProductCommand(
                                productId, "Updated Headphones", new BigDecimal("129.99"), 75));
    }

    @Test
    void shouldCorrectlyAccessDeleteProductEP() throws Exception {
        var productId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(DeleteProductCommand.class)))
                .thenReturn(IdResult.of(productId));

        mockMvc.perform(delete("/api/products/" + productId)).andExpect(status().isNoContent());

        verify(commandBus).execute(new DeleteProductCommand(productId));
    }
}
