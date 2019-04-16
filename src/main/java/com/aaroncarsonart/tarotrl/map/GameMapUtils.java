package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Maze;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TODO: Not used.
 * So, feel free to rewrite/delete/repurpose, etc.
 */
public class GameMapUtils {
//    int width;
//    int height;
//    public Tile[] tiles;
//
//    public GameMapUtils(int width, int height) {
//        this.width = width;
//        this.height = height;
//        this.tiles = new Tile[width * height];
//    }
//
//    public void setTile(int x, int y, Tile tile) {
//        tiles[x + y * width] = tile;
//    }
//
//    public Tile getTile(int x, int y) {
//        return tiles[x + y * width];
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder(width * height);
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                Tile tile = getTile(x, y);
//                char sprite = tile.sprite;
//                builder.append(sprite);
//            }
//            builder.append("\n");
//        }
//        return builder.toString();
//    }

    public static final char[][] MAP_10_BY_10_ZEROS = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public static final char[][] MAP_10_BY_12_A = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };


    public static void printMap2D(char[][] map, int height, int width) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char next = map[y][x];
                System.out.print(next);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

//    public static char[][] TEST_MAP_40_BY_20 = readMapFile("maps/test_map.txt");

    /**
     * Retroactively calculate a 2d char array map's height.
     * @param map The map to calculate the height of.
     * @return The height of the map.
     */
    public static int getMapHeight(char[][] map) {
        // The map's height is the number of columns,
        // which is the length of the outer array.
        int mapHeight = map.length;
        return mapHeight;
    }

    /**
     * Retroactively calculate a 2d char array map's width.
     * Use sparingly, as it must iterate over the rows.
     * @param map The map to calculate the width of.
     * @return The width of the map.
     */
    public static int getMapWidth(char[][] map) {
        // The map's width is the number of rows,
        // which is the length of the longest inner array.
        int mapWidth = 0;
        for (char[] row : map) {
            mapWidth = Math.max(mapWidth, row.length);
        }
        return mapWidth;
    }

    /**
     * Check if the given origin is empty (accessable to be moved upon).
     * @param map The map to check.
     * @param py The y coordinate.
     * @param px The x coordinate.
     * @return True, if movable.
     */
    public static boolean isEmpty(char[][] map, int py, int px) {
        return false;
    }

    /**
     * Attempt to read a simple 2d char array from a text file.
     * @param mapResourceRelativeFilePath The path to read the file from, within the resources directory.
     * @return
     */
    public static char[][] readMapFile(String mapResourceRelativeFilePath) {
        URL mapFileUrl = GameMapUtils.class.getResource(mapResourceRelativeFilePath);
        try {
            Scanner s = new Scanner(mapFileUrl.openStream());

            int maxLength = 0;
            List<String> lines = new ArrayList<>();

            while (s.hasNextLine()) {
                String line = s.nextLine();
                maxLength = Math.max(maxLength, line.length());
                lines.add(line);
            }

            int mapWidth = maxLength;
            int mapHeight = lines.size();
            char[][] mapFileContents = new char[mapHeight][mapWidth];

            for (int y = 0; y < mapHeight; y ++) {
                String row = lines.get(y);
                int rowWidth = row.length();
                for (int x = 0; x < rowWidth; x ++) {
                    mapFileContents[y][x] = row.charAt(x);
                }
            }

            return mapFileContents;
        } catch (IOException ex) {
            throw new RuntimeException("Error reading: " + mapResourceRelativeFilePath);
        }
    }


    /**
     * Attempt to read a proper GameMap object from a text file, following some presumed conventions.
     *
     * @param mapResourceRelativeFilePath The path to read the file from, within the resources directory.
     * @return
     */
    public static GameMap readFileAsGameMap(String mapResourceRelativeFilePath) {
        URL mapFileUrl = GameMapUtils.class.getResource(mapResourceRelativeFilePath);
        try {
            Scanner scanner = new Scanner(mapFileUrl.openStream());

            // First, read the name of this map.  Note that it must be unique!
            String gameMapName = null;
            if (containsHeader(scanner, "Map Name")) {
                gameMapName = scanner.nextLine();
            }

            // consume blank line
            scanner.nextLine();

            // read the map data
            char[][] mapTileData = null;
            int mapHeight = 0;
            int mapWidth = 0;
            if (containsHeader(scanner, "Tile Data")) {
                int maxLength = 0;
                List<String> lines = new ArrayList<>();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.length() > 0) {
                        maxLength = Math.max(maxLength, line.length());
                        lines.add(line);
                    } else {
                        break;
                    }
                }

                mapWidth = maxLength;
                mapHeight = lines.size();
                mapTileData = new char[mapHeight][mapWidth];

                for (int y = 0; y < mapHeight; y ++) {
                    String row = lines.get(y);
                    int rowWidth = row.length();
                    for (int x = 0; x < rowWidth; x ++) {
                        mapTileData[y][x] = row.charAt(x);
                    }
                }
            }

            GameMap gameMap = new GameMap(
                    gameMapName,
                    mapTileData,
                    mapHeight,
                    mapWidth);

            return gameMap;

        } catch (IOException ex) {
            throw new RuntimeException("Error reading: " + mapResourceRelativeFilePath);
        }
    }

    /**
     * The format is:
     * @param scanner The input scanner to read from.
     * @return The name of the OldTarotRLGame map, from the input file read by the scanner.
     */
    private static boolean containsHeader(Scanner scanner, String headerString) {
        // Consume the necessary header, erroring out if not found!
        boolean hasMapNameHeader = scanner.nextLine().startsWith(headerString);

        // This regex matches only exactly a line made of dashes: "--------"
        boolean hasDottedLine = scanner.nextLine().matches("^[-].+$");

        if (hasMapNameHeader && hasDottedLine) {
            return true;
        } else {
            throw new RuntimeException("GameMap file missing map name.");
        }
    }

    public static GameMap createGameMapFromMaze(String name, Maze maze) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        char[][] tileGrid = new char[height][width];
        for (int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                byte mazeCell = maze.getCell(x, y);
                char tileSprite;
                if (mazeCell == Maze.PATH) {
                    tileSprite = GameSprite.PATH;
                } else if (mazeCell == Maze.WALL) {
                    tileSprite = GameSprite.WALL;
                } else {
                    tileSprite = GameSprite.EMPTY;
                }
                tileGrid[y][x] = tileSprite;
            }
        }
        GameMap gameMap = new GameMap(name, tileGrid, height, width);
        return gameMap;
    }
}
