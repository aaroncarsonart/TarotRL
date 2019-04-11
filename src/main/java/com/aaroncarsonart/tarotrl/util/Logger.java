package com.aaroncarsonart.tarotrl.util;

public class Logger {
    public static final boolean ENABLE_ERROR_LEVEL = true;
    public static final boolean ENABLE_WARNING_LEVEL = true;
    public static final boolean ENABLE_INFO_LEVEL = true;
    public static final boolean ENABLE_DEBUG_LEVEL = false;
    public static final boolean ENABLE_TRACE_LEVEL = false;
    public static final boolean ENABLE_TESTING_LEVEL = false;

    private Class clazz;

    public Logger (Class clazz) {
        this.clazz = clazz;
    }

    private enum LogLevel {
        ERROR(ENABLE_ERROR_LEVEL),
        WARNING(ENABLE_WARNING_LEVEL),
        INFO(ENABLE_INFO_LEVEL),
        DEBUG(ENABLE_DEBUG_LEVEL),
        TRACE(ENABLE_TRACE_LEVEL),
        TESTING(ENABLE_TESTING_LEVEL);

        final boolean enabled;

        LogLevel(boolean enabled) {
            this.enabled = enabled;
        }
    }

    private String prefix(LogLevel level) {
        return level.name() + " " + clazz.getName() + ": ";
    }

    private void log(LogLevel level, Object obj) {
        if (level.enabled) {
            System.out.print(prefix(level));
            System.out.println(obj);
        }
    }

    public void error(Object obj) {
        log(LogLevel.ERROR, obj);
    }

    public void warning(Object obj) {
        log(LogLevel.WARNING, obj);
    }

    public void info(Object obj) {
        log(LogLevel.INFO, obj);
    }

    public void debug(Object obj) {
        log(LogLevel.DEBUG, obj);
    }

    public void trace(Object obj) {
        log(LogLevel.TRACE, obj);
    }

    public void testing(Object obj) {
        log(LogLevel.TESTING, obj);
    }

}
