package cz.rohlik.commerce.adapter.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cz.rohlik.commerce.ControllerTest;
import cz.rohlik.commerce.TestUtils;
import cz.rohlik.commerce.application.common.command.IdResult;
import cz.rohlik.commerce.application.module.product.command.CreateProductCommand;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(ProductController.class)
class ProductControllerTest extends ControllerTest {

    @Test
    void shouldCorrectlyAccessCreateProductEP() throws Exception {
        var expectedProductId = TestUtils.createUuidFromNumber(1);
        when(commandBus.execute(any(CreateProductCommand.class)))
                .thenReturn(IdResult.of(expectedProductId));

        mockMvc.perform(
                        TestUtils.addJsonContent(
                                post("/products"),
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
}
