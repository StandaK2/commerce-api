package cz.rohlik.commerce.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging configuration and utilities for the Commerce API application.
 * Provides convenient logger creation methods.

 */
public final class LoggingConfig {

    private LoggingConfig() {
        // Utility class
    }

    /**
     * Creates a logger for the given class.
     * Convenient static method for logger creation.
     *
     * @param clazz The class to create logger for
     * @return SLF4J Logger instance
     */
    public static Logger loggerFor(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Creates a logger for the calling class.
     * Uses stack walking to determine the caller class automatically.
     * Note: This is more expensive than loggerFor(Class) but more convenient.
     *
     * @return SLF4J Logger instance for the calling class
     */
    public static Logger logger() {
        return LoggerFactory.getLogger(StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass());
    }
}
