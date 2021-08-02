package com.aaroncarsonart.tarotrl.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextUtils {

    public static List<String> getWordWrappedText(String text, int maxWidth) {
        List<String> finalList = new ArrayList<>();
        String[] initialWordWrap = text.split("\n");
        for (String segment : initialWordWrap) {
            String wrappedText = WordUtils.wrap(segment, maxWidth);
            String[] wrappedTextArray = wrappedText.split("\n");
            for (String finalSegment : wrappedTextArray) {
                finalList.add(finalSegment);
            }
        }
        return finalList;
    }

    public static String getLowerCaseStringWithSpaces(String text) {
        return text.toLowerCase().replaceAll("_", " ");
    }

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String getStringOfLength(char c, int length) {
        return IntStream.range(0, length)
                .mapToObj(j -> String.valueOf(c))
                .collect(Collectors.joining());
    }

    public static String align(String text, int length, TextAlignment alignment) {
        switch (alignment) {
            default:
            case LEFT: return StringUtils.rightPad(text, length);
            case RIGHT: return  StringUtils.leftPad(text, length);
            case CENTER: return  StringUtils.center(text, length);
        }
    }
}
