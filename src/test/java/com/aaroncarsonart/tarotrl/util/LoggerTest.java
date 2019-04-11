package com.aaroncarsonart.tarotrl.util;

import org.junit.jupiter.api.Test;

class LoggerTest {
    private static final Logger LOG = new Logger(LoggerTest.class);

    @Test
    void testLoggingMethods() {

        int a = 2;
        Character b = 'b';
        Object c = null;
        double d = 3.14159;
        String e = "PI";

        LOG.error(  "error message");
        LOG.warning("warning message");
        LOG.info(   "info message");
        LOG.debug(  "debug message");
        LOG.trace(  "trace message");
        LOG.logTest("logTest message");

        LOG.info(a);
        LOG.info(b);
        LOG.info(c);
        LOG.info(d);
        LOG.info(e);

        LOG.error(  "Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
        LOG.warning("Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
        LOG.info(   "Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
        LOG.debug(  "Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
        LOG.trace(  "Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
        LOG.logTest("Formatted message. a: %d b: %c c: %s d: %6.2f e: %-5s", a, b, c, d, e);
    }

}