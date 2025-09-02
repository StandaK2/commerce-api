package cz.rohlik.commerce.application.common.query;

/**
 * Query bus interface for dispatching queries to their handlers. Provides a unified entry point for
 * executing queries in the CQRS architecture.
 */
public interface QueryBus {

    /**
     * Executes a query by dispatching it to the appropriate handler.
     *
     * @param <R> The result type
     * @param query The query to execute
     * @return The result of query execution
     */
    <R> R execute(Query<R> query);
}
