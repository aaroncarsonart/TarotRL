package com.aaroncarsonart.tarotrl.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Logger {
    public static final boolean ENABLE_ERROR_LEVEL   = true;
    public static final boolean ENABLE_WARNING_LEVEL = true;
    public static final boolean ENABLE_INFO_LEVEL    = true;
    public static final boolean ENABLE_DEBUG_LEVEL   = true;
    public static final boolean ENABLE_TRACE_LEVEL   = false;
    public static final boolean ENABLE_LOG_TEST_LEVEL = false;

    private static final String LOG_PREFIX_FORMAT = "%s %-7s %s - ";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss.SSS");

    private Class clazz;
    private boolean loggerEnabled = true;
    private LogLevel logLevel = LogLevel.LOG_TEST;

    public Logger (Class clazz) {
        this.clazz = clazz;
    }

    private String prefix(LogLevel level) {
        return level.name() + " " + clazz.getName() + ": ";
    }

    private String getTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    private void log(LogLevel level, Object obj) {
        if (loggerEnabled && level.enabled && level.ordinal() <= logLevel.ordinal()) {
            System.out.printf(LOG_PREFIX_FORMAT, getTimeStamp(), level, clazz.getName());
            System.out.println(obj);
        }
    }

    private void logf(LogLevel level, String format, Object ... args) {
        if (loggerEnabled && level.enabled && level.ordinal() <= logLevel.ordinal()) {
            System.out.printf(LOG_PREFIX_FORMAT, getTimeStamp(), level, clazz.getName());
            System.out.println(String.format(format, args));
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

    public void error(String format, Object ... args) {
        logf(LogLevel.ERROR, format, args);
    }

    public void warning(String format, Object ... args) {
        logf(LogLevel.WARNING, format, args);
    }

    public void info(String format, Object ... args) {
        logf(LogLevel.INFO, format, args);
    }

    public void debug(String format, Object ... args) {
        logf(LogLevel.DEBUG, format, args);
    }

    public void trace(String format, Object ... args) {
        logf(LogLevel.TRACE, format, args);
    }

    public void logTest(Object obj) {
        log(LogLevel.LOG_TEST, obj);
    }

    public void logTest(String format, Object ... args) {
        logf(LogLevel.LOG_TEST, format, args);
    }

    public void disable() {
        this.loggerEnabled = false;
    }

    public void enable() {
        this.loggerEnabled = true;
    }

    public Logger disabled() {
        disable();
        return this;
    }

    public Logger enabled() {
        enable();
        return this;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public Logger withLogLevel(LogLevel logLevel) {
        setLogLevel(logLevel);
        return this;
    }
}
