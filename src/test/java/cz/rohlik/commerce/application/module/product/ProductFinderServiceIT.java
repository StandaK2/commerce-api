package cz.rohlik.commerce.application.module.product;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.IntegrationTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductFinderServiceIT extends IntegrationTest {

    @Test
    void shouldCorrectlyIdentifyProductsWithActiveOrders() {
        var productNoOrders =
                testDataHelper.createProduct("Product No Orders", new BigDecimal("10.00"), 50);

        var productWithActiveOrder =
                testDataHelper.createProduct("Product Active Order", new BigDecimal("20.00"), 50);
        var activeOrder = testDataHelper.createDefaultOrder();
        testDataHelper.createOrderItem(
                activeOrder.getId(), productWithActiveOrder.getId(), 2, new BigDecimal("20.00"));

        assertThat(productFinderService.hasActiveOrders(productNoOrders.getId())).isFalse();
        assertThat(productFinderService.hasActiveOrders(productWithActiveOrder.getId())).isTrue();
    }
}
