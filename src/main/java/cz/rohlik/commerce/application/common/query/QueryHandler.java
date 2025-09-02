package cz.rohlik.commerce.application.common.query;

/**
 * Interface for query handlers in the CQRS architecture. Each query handler is responsible for
 * processing a specific query type.
 *
 * @param <RESULT> The type of result returned by the query
 * @param <QUERY> The specific query type this handler processes
 */
public interface QueryHandler<RESULT, QUERY extends Query<RESULT>> {

    /**
     * Gets the query class this handler processes. Used for query routing in the query bus.
     *
     * @return The Class object for the query type
     */
    Class<QUERY> getQueryClass();

    /**
     * Handles the execution of the query.
     *
     * @param query The query to execute
     * @return The result of query execution
     */
    RESULT handle(QUERY query);
}
