package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.map.json.GameTileDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private Map<Character, GameTileDefinition> tileSprites;
    private char outOfBoundsTile = '#';

    /**
     * Any tiles that need re-rendering, as the game state has changed.
     */
    private List<Position2D> dirtyTiles = new ArrayList<>();

    public GameMap(String name, char[][] tileGrid, int height, int width) {
        this.name = name;
        this.tileGrid = tileGrid;
        this.height = height;
        this.width = width;
    }

    private GameMap() {
    }

    public Map<Character, GameTileDefinition> getTileSprites() {
        return tileSprites;
    }

    public void setTileSprites(Map<Character, GameTileDefinition> tileSprites) {
        this.tileSprites = tileSprites;
    }

    public char getOutOfBoundsTile() {
        return outOfBoundsTile;
    }

    public void setOutOfBoundsTile(char outOfBoundsTile) {
        this.outOfBoundsTile = outOfBoundsTile;
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
        char sprite = tileGrid[py][px];
        GameTileDefinition tile = tileSprites.get(sprite);
        return tile.isPassable();
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

    public List<Position2D> getDirtyTiles() {
        return dirtyTiles;
    }

    public char getTile(int y, int x) {
        return tileGrid[y][x];
    }

    public char getTile(Position2D pos) {
        return getTile(pos.y(), pos.x());
    }

    public void setTile(Position2D pos, TileType newTileType) {
        tileGrid[pos.y()][pos.x()] = newTileType.getSprite();
        dirtyTiles.add(pos);
    }

    public GameTileDefinition getTileDefinition(Position2D pos) {
        char tileSprite;
        if (withinBounds(pos)) {
            tileSprite = getTile(pos);
        } else {
            tileSprite = outOfBoundsTile;
        }
        GameTileDefinition tileDefinition = tileSprites.get(tileSprite);
        return tileDefinition;
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
