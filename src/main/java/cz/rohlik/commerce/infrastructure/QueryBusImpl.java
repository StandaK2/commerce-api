package cz.rohlik.commerce.infrastructure;

import cz.rohlik.commerce.application.common.query.Query;
import cz.rohlik.commerce.application.common.query.QueryBus;
import cz.rohlik.commerce.application.common.query.QueryHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/** Query bus implementation for CQRS pattern. Routes queries to their appropriate handlers. */
@Component
public class QueryBusImpl implements QueryBus {

    private final Map<String, QueryHandler<?, ?>> handlers = new HashMap<>();

    public QueryBusImpl(List<QueryHandler<?, ?>> queryHandlers) {
        queryHandlers.forEach(
                handler -> {
                    String queryName = handler.getQueryClass().getName();
                    if (handlers.containsKey(queryName)) {
                        throw new IllegalStateException(
                                "Multiple handlers for query: " + queryName);
                    }
                    handlers.put(queryName, handler);
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R execute(Query<R> query) {
        String queryName = query.getClass().getName();
        var handler = handlers.get(queryName);

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for query: " + queryName);
        }

        return ((QueryHandler<R, Query<R>>) handler).handle(query);
    }
}
