package com.aaroncarsonart.tarotrl.util;

import org.junit.jupiter.api.Test;

import java.util.List;

class TextUtilsTest {
    private static final Logger LOG = new Logger(TextUtilsTest.class);

    @Test
    void testWordWrap() {

        String original = "I am so many characters long";
        int maxLength = 5;
        List<String> wrappedText = TextUtils.getWordWrappedText(original, maxLength);
        for (String line: wrappedText) {
            LOG.testing(line);
        }

        LOG.testing("");

        original = "Where you are standing, there is an open path.";
        maxLength = 44;
        wrappedText = TextUtils.getWordWrappedText(original, maxLength);
        for (String line: wrappedText) {
            LOG.testing(line);
        }
    }
}