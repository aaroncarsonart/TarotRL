package com.aaroncarsonart.tarotrl.map;

import java.util.Arrays;
import java.util.List;

public class GameSprite {
    public static final char PLAYER = '@';
    public static final char WALL = '#';
    public static final char PATH = '.';
    public static final char EMPTY = ' ';
    public static final char DOWNSTAIRS = '>';
    public static final char UPSTAIRS = '<';
    public static final char CLOSED_DOOR = '+';
    public static final char OPEN_DOOR   = '-';
    public static final char TREASURE = '$';
//    public static final char TREASURE = '$';


    public static final List<Character> PASSABLE_TILES = Arrays.asList(PATH);
    public static final String IMPASSABLE_TILES = fromSprites(WALL);

    private static final String fromSprites(char ... sprites) {
        return new String(sprites);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 255; i++) {
            char c = (char) i;
            System.out.printf("%3d %c\n", i, c);
        }
    }

}
