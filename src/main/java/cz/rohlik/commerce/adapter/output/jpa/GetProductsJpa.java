package cz.rohlik.commerce.adapter.output.jpa;

import cz.rohlik.commerce.application.module.product.port.output.GetProducts;
import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetProductsJpa implements GetProducts {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<GetProductsQuery.Result> invoke() {
        String jpql =
                """
            SELECT new cz.rohlik.commerce.application.module.product.query.GetProductsQuery$Result(
                p.id,
                p.name,
                p.price,
                p.stockQuantity,
                p.createdAt,
                COALESCE(SUM(CASE WHEN o.status = 'PAID' THEN oi.quantity ELSE 0L END), 0L),
                COALESCE(SUM(CASE WHEN o.status = 'PAID' THEN oi.quantity * oi.unitPrice ELSE 0 END), 0)
            )
            FROM Product p
            LEFT JOIN OrderItem oi ON p.id = oi.productId
            LEFT JOIN Order o ON oi.orderId = o.id
            WHERE p.deletedAt IS NULL
            GROUP BY p.id, p.name, p.price, p.stockQuantity, p.createdAt
            ORDER BY p.createdAt DESC
            """;

        TypedQuery<GetProductsQuery.Result> query =
                entityManager.createQuery(jpql, GetProductsQuery.Result.class);
        return query.getResultList();
    }
}
