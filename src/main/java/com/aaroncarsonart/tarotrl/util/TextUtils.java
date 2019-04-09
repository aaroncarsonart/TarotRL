package com.aaroncarsonart.tarotrl.util;

import org.apache.commons.text.WordUtils;

import java.util.Arrays;
import java.util.List;

public class TextUtils {

    public static List<String> getWordWrappedText(String text, int maxWidth) {
        String wrappedText = WordUtils.wrap(text, maxWidth);
        String[] wrappedTextArray = wrappedText.split("\n");
        return Arrays.asList(wrappedTextArray);
    }

    public static String getLowerCaseStringWithSpaces(String text) {
        return text.toLowerCase().replaceAll("_", " ");
    }

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
