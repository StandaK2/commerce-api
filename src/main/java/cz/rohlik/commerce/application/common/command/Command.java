package cz.rohlik.commerce.application.common.command;

/**
 * Base interface for all commands in the CQRS architecture. Commands represent write operations
 * that change system state.
 *
 * @param <RESULT> The type of result returned by the command
 */
public interface Command<RESULT> {
    // Marker interface - no methods required
}
