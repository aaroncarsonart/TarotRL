package com.aaroncarsonart.tarotrl.util;

import com.aaroncarsonart.imbroglio.Difficulty;
import com.aaroncarsonart.imbroglio.Maze;
import org.junit.jupiter.api.Test;

import java.util.List;

class TextUtilsTest {

    @Test
    void testWordWrap() {

        String original = "I am so many characters long";
        int maxLength = 5;
        List<String> wrappedText = TextUtils.getWordWrappedText(original, maxLength);
        for (String line: wrappedText) {
            System.out.println(line);
        }

        System.out.println();

        original = "Where you are standing, there is an open path.";
        maxLength = 44;
        wrappedText = TextUtils.getWordWrappedText(original, maxLength);
        for (String line: wrappedText) {
            System.out.println(line);
        }
    }

    @Test
    void runtimeForMazeGeneration() {

        int width = 100;
        int height = 100;

        long begin, end, elapsed;

        begin = System.currentTimeMillis();
        Maze maze = Maze.generateRandomWalledMaze(width, height);
        end = System.currentTimeMillis();
        elapsed = end - begin;
        System.out.println(elapsed);

        begin = System.currentTimeMillis();
        maze.setDifficulty(Difficulty.EASY);
        end = System.currentTimeMillis();
        elapsed = end - begin;
        System.out.println(elapsed);

    }
}