package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.application.common.query.QueryHandler;
import cz.rohlik.commerce.application.module.product.port.output.GetProducts;
import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductsQueryHandler
        implements QueryHandler<List<GetProductsQuery.Result>, GetProductsQuery> {

    private final GetProducts getProducts;

    @Override
    public Class<GetProductsQuery> getQueryClass() {
        return GetProductsQuery.class;
    }

    @Override
    public List<GetProductsQuery.Result> handle(GetProductsQuery query) {
        return getProducts.invoke();
    }
}
