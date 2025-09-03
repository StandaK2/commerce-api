package cz.rohlik.commerce.application.module.product;

import static org.assertj.core.api.Assertions.assertThat;

import cz.rohlik.commerce.IntegrationTest;
import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import cz.rohlik.commerce.domain.model.order.constant.OrderStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GetProductsQueryHandlerIT extends IntegrationTest {

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        var result = queryBus.execute(new GetProductsQuery());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnProductsWithCorrectAggregatesAndOrdering() {
        // Create test products
        var productWithNoOrders =
                testDataHelper.createProduct("Product No Orders", new BigDecimal("10.00"), 50);
        var productWithSales =
                testDataHelper.createProduct("Product With Sales", new BigDecimal("20.00"), 30);
        var deletedProduct =
                testDataHelper.createDeletedProduct("Deleted Product", new BigDecimal("15.00"), 25);
        var newestProduct =
                testDataHelper.createProduct("Newest Product", new BigDecimal("30.00"), 40);

        // Create first PAID order with sales data
        var paidOrder1 = testDataHelper.createOrder(OrderStatus.PAID);
        testDataHelper.createOrderItem(
                paidOrder1.getId(), productWithSales.getId(), 3, new BigDecimal("20.00"));
        testDataHelper.createOrderItem(
                paidOrder1.getId(), newestProduct.getId(), 2, new BigDecimal("30.00"));

        // Create second PAID order with more sales data
        var paidOrder2 = testDataHelper.createOrder(OrderStatus.PAID);
        testDataHelper.createOrderItem(
                paidOrder2.getId(),
                productWithSales.getId(),
                2,
                new BigDecimal("22.00")); // Different price
        testDataHelper.createOrderItem(
                paidOrder2.getId(), newestProduct.getId(), 1, new BigDecimal("30.00"));

        // Create CREATED order (should not be counted in aggregates)
        var createdOrder = testDataHelper.createOrder(OrderStatus.CREATED);
        testDataHelper.createOrderItem(
                createdOrder.getId(), productWithSales.getId(), 5, new BigDecimal("20.00"));
        testDataHelper.createOrderItem(
                createdOrder.getId(), productWithNoOrders.getId(), 1, new BigDecimal("10.00"));

        var result = queryBus.execute(new GetProductsQuery());

        // Should return 3 products (excluding deleted)
        assertThat(result).hasSize(3);

        // Check ordering (newest first)
        assertThat(result.get(0).id()).isEqualTo(newestProduct.getId());
        assertThat(result.get(1).id()).isEqualTo(productWithSales.getId());
        assertThat(result.get(2).id()).isEqualTo(productWithNoOrders.getId());

        // Check newest product (has sales from 2 PAID orders: 2+1=3 items, 60.00+30.00=90.00)
        var newestResult = result.get(0);
        assertThat(newestResult.name()).isEqualTo("Newest Product");
        assertThat(newestResult.price()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(newestResult.stockQuantity()).isEqualTo(40);
        assertThat(newestResult.soldCount()).isEqualTo(3L); // 2 + 1 from both PAID orders
        assertThat(newestResult.soldSum())
                .isEqualByComparingTo(new BigDecimal("90.00")); // (2*30.00) + (1*30.00)

        // Check product with sales (has sales from 2 PAID orders: 3+2=5 items, 60.00+44.00=104.00)
        var salesResult = result.get(1);
        assertThat(salesResult.name()).isEqualTo("Product With Sales");
        assertThat(salesResult.price()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(salesResult.stockQuantity()).isEqualTo(30);
        assertThat(salesResult.soldCount())
                .isEqualTo(5L); // 3 + 2 from both PAID orders (ignoring CREATED order)
        assertThat(salesResult.soldSum())
                .isEqualByComparingTo(new BigDecimal("104.00")); // (3*20.00) + (2*22.00)

        // Check product with no orders (zero aggregates)
        var noOrdersResult = result.get(2);
        assertThat(noOrdersResult.name()).isEqualTo("Product No Orders");
        assertThat(noOrdersResult.price()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(noOrdersResult.stockQuantity()).isEqualTo(50);
        assertThat(noOrdersResult.soldCount()).isEqualTo(0L);
        assertThat(noOrdersResult.soldSum()).isEqualByComparingTo(BigDecimal.ZERO);

        // Verify deleted product is not included (already checked by size assertion)
        assertThat(result).noneMatch(p -> p.id().equals(deletedProduct.getId()));
    }
}
