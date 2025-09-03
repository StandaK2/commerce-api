package cz.rohlik.commerce.application.module.order.command;

import cz.rohlik.commerce.application.common.command.Command;
import cz.rohlik.commerce.application.common.command.IdResult;

public record InitOrderCommand() implements Command<IdResult> {}
