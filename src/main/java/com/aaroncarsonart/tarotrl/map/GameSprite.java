package com.aaroncarsonart.tarotrl.map;

import java.util.Arrays;
import java.util.List;

public class GameSprite {
    public static final char PLAYER = '@';
    public static final char WALL = '#';
    public static final char PATH = '.';
    public static final char EMPTY = ' ';

    public static final List<Character> PASSABLE_TILES = Arrays.asList(PATH);
    public static final String IMPASSABLE_TILES = fromSprites(WALL);

    private static final String fromSprites(char ... sprites) {
        return new String(sprites);
    }
}
