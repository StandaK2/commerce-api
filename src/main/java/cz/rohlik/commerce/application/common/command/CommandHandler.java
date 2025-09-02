package cz.rohlik.commerce.application.common.command;

/**
 * Interface for command handlers in the CQRS architecture. Each command handler is responsible for
 * processing a specific command type.
 *
 * @param <RESULT> The type of result returned by the command
 * @param <COMMAND> The specific command type this handler processes
 */
public interface CommandHandler<RESULT, COMMAND extends Command<RESULT>> {

    /**
     * Gets the command class this handler processes. Used for command routing in the command bus.
     *
     * @return The Class object for the command type
     */
    Class<COMMAND> getCommandClass();

    /**
     * Handles the execution of the command.
     *
     * @param command The command to execute
     * @return The result of command execution
     */
    RESULT handle(COMMAND command);
}
