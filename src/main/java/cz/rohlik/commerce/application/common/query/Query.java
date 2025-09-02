package cz.rohlik.commerce.application.common.query;

/**
 * Base interface for all queries in the CQRS architecture. Queries represent read operations that
 * don't change system state.
 *
 * @param <RESULT> The type of result returned by the query
 */
public interface Query<RESULT> {
    // Marker interface - no methods required
}
