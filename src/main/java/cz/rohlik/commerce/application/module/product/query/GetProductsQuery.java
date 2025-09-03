package cz.rohlik.commerce.application.module.product.query;

import cz.rohlik.commerce.application.common.query.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetProductsQuery() implements Query<List<GetProductsQuery.Result>> {

    @Schema(name = "GetProductsResult")
    public record Result(
            UUID id,
            String name,
            BigDecimal price,
            Integer stockQuantity,
            Instant createdAt,
            Long soldCount,
            BigDecimal soldSum) {}
}
