package cz.rohlik.commerce.infrastructure;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.CommandBus;
import cz.rohlik.commerce.application.common.command.CommandHandler;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Command bus implementation for CQRS pattern. Routes commands to their appropriate handlers with
 * validation.
 */
@Component
@Validated
public class CommandBusImpl implements CommandBus {

    private final Map<String, CommandHandler<?, ?>> handlers = new HashMap<>();

    public CommandBusImpl(List<CommandHandler<?, ?>> commandHandlers) {
        commandHandlers.forEach(
                handler -> {
                    String commandName = handler.getCommandClass().getName();
                    if (handlers.containsKey(commandName)) {
                        throw new IllegalStateException(
                                "Multiple handlers for command: " + commandName);
                    }
                    handlers.put(commandName, handler);
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R execute(@Valid Command<R> command) {
        String commandName = command.getClass().getName();
        var handler = handlers.get(commandName);

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for command: " + commandName);
        }

        return ((CommandHandler<R, Command<R>>) handler).handle(command);
    }
}
