package cz.rohlik.commerce.application.common.command;

import jakarta.validation.Valid;

/**
 * Command bus interface for dispatching commands to their handlers. Provides a unified entry point
 * for executing commands in the CQRS architecture.
 */
public interface CommandBus {

    /**
     * Executes a command by dispatching it to the appropriate handler. The command is validated
     * before execution.
     *
     * @param <R> The result type
     * @param command The command to execute (will be validated)
     * @return The result of command execution
     */
    <R> R execute(@Valid Command<R> command);
}
