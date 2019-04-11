package com.aaroncarsonart.tarotrl.util;

public interface Loggable {
    boolean ENABLE_ERROR_LEVEL = true;
    boolean ENABLE_WARNING_LEVEL = true;
    boolean ENABLE_INFO_LEVEL = true;
    boolean ENABLE_DEBUG_LEVEL = true;

    enum LogLevel {
        ERROR(ENABLE_ERROR_LEVEL),
        WARNING(ENABLE_WARNING_LEVEL),
        INFO(ENABLE_INFO_LEVEL),
        DEBUG(ENABLE_DEBUG_LEVEL);

        final boolean enabled;

        LogLevel(boolean enabled) {
            this.enabled = enabled;
        }
    }

    default String prefix(LogLevel level) {
        return level.name() + " " + getClass().getName() + ": ";
    }

    default void log(LogLevel level, String text) {
        if (level.enabled) {
            System.out.print(prefix(level));
            System.out.println(text);
        }
    }

    default void error(String text) {
        log(LogLevel.DEBUG, text);
    }

    default void warning(String text) {
        log(LogLevel.DEBUG, text);
    }

    default void info(String text) {
        log(LogLevel.DEBUG, text);
    }

    default void debug(String text) {
        log(LogLevel.DEBUG, text);
    }

}
