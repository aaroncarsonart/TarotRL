package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Position2D;

import java.io.Serializable;

/**
 * Encapsulates essential 2d tileGrid information in one class.
 *
 * This coordinate plane always starts with (0,0) as the top-left corner,
 * with (mapHeight, mapWidth) as the bottom right corner of the tileGrid.
 */
public class GameMap implements Serializable {

    private String name;
    private char[][] tileGrid;

    private int height;  // NOTE: height/rows are equivalent terminology
    private int width;   // NOTE: width/columns are equivalent terminology

    public GameMap(String name, char[][] tileGrid, int height, int width) {
        this.name = name;
        this.tileGrid = tileGrid;
        this.height = height;
        this.width = width;
    }

    private GameMap() {
    }

    public GameMap createDeepCopy() {
        GameMap copy = new GameMap();

        copy.name = this.name;
        copy.tileGrid = this.getTileGridCopy();
        copy.height = this.height;
        copy.width = this.width;

        return copy;
    }

    public char[][] getTileGridCopy() {
        char[][] copy = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                copy[y][x] = tileGrid[y][x];
            }
        }
        return copy;
    }

    /**
     * Is the given position within the bounds of the tileGrid?
     * @param py The y coordinate to check
     * @param px The x coordinate to check
     * @return True, if the position lands on the tileGrid; otherwise, false.
     */
    public boolean withinBounds(int py, int px) {
        return (0 <= py && py < height) &&
                (0 <= px && px < width);
    }

    public boolean withinBounds(Position2D pos) {
        return withinBounds(pos.y(), pos.x());
    }

    /**
     * Is the given position within the bounds of the tileGrid?
     * @param py The y coordinate to check
     * @param px The x coordinate to check
     * @return True, if the position lands on the tileGrid; otherwise, false.
     */
    public boolean isPassable(int py, int px) {
        char tile = tileGrid[py][px];
        return GameSprite.PASSABLE_TILES.contains(tile);
    }

    public boolean isPassable(Position2D pos) {
        return isPassable(pos.y(), pos.x());
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return getName();
    }

    public char getTile(int y, int x) {
        return tileGrid[y][x];
    }

    /**
     * Get the top-left most position of this map that matches the given character.
     *
     * @param character The type of cell to find.
     * @return The top, left-most unvisited position, or null if not found.
     */
    public Position2D findFirstOccurrence(char character) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tileGrid[y][x] == character) {
                    return new Position2D(x, y);
                }
            }
        }
        return null;
    }

    public String createTileDataString() {
        // go ahead and make room for spaces and newlines
        int len = (width * 2 + 1) * height;
        StringBuilder sb = new StringBuilder(len);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char next = tileGrid[y][x];
                sb.append(next);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String toString() {
        return createTileDataString();
    }
}
