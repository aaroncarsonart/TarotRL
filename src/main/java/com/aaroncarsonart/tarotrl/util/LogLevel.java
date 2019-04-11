package com.aaroncarsonart.tarotrl.util;

public enum LogLevel {
    ERROR(Logger.ENABLE_ERROR_LEVEL),
    WARNING(Logger.ENABLE_WARNING_LEVEL),
    INFO(Logger.ENABLE_INFO_LEVEL),
    DEBUG(Logger.ENABLE_DEBUG_LEVEL),
    TRACE(Logger.ENABLE_TRACE_LEVEL),
    LOG_TEST(Logger.ENABLE_LOG_TEST_LEVEL);

    final boolean enabled;

    LogLevel(boolean enabled) {
        this.enabled = enabled;
    }
}
