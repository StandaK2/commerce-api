package cz.rohlik.commerce.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging configuration and utilities for the Commerce API application. Provides convenient logger
 * creation methods.
 */
public final class LoggingConfig {

    private LoggingConfig() {
        // Utility class
    }

    public static Logger logger() {
        return LoggerFactory.getLogger(
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass());
    }
}
