package cz.rohlik.commerce.application.module.product.port.output;

import cz.rohlik.commerce.application.module.product.query.GetProductsQuery;
import java.util.List;

public interface GetProducts {
    List<GetProductsQuery.Result> invoke();
}
