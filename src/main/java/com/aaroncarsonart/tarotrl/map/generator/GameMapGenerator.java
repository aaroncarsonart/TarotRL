package com.aaroncarsonart.tarotrl.map.generator;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.GameTileDefinition;

import java.util.Map;

/**
 * Encapsulates logic for Generating GameMaps from GameMapDefinitions.
 */
public class GameMapGenerator {

    /**
     * This is the main outward facing interface of this class.
     *
     * @param definition The GameMapDefinition to generateMapFrom the GameMap from.
     * @return The generated GameMap that is defined by the GameMapDefinition.
     */
    public GameMap generateMapFrom(GameMapDefinition definition) {

        String mapName = definition.getMapName();
        int width = definition.getWidth();
        int height = definition.getHeight();

        char[][] terrainGrid;

        switch (definition.getMapType()) {

            case VAULT:
                String[] lines = definition.getMapTerrainData();
                terrainGrid = createGridFrom(lines);
                break;

            case MAZE:
                Maze maze = Maze.generateRandomWalledMaze(width, height);
                maze.setDifficulty(definition.getMazeDifficulty());
                width = maze.getWidth();
                height = maze.getHeight();
                terrainGrid = createGridFrom(maze);
                break;

            case CELLULAR_AUTOMATA:
                int additionalIterations = definition.getIterations();
                maze = generateCellularAutomataRoom(width, height, additionalIterations);
                terrainGrid = createGridFrom(maze);
                break;

            case RANDOM:
                double digPercentage = definition.getDigPercentage();
                boolean useTunnels = definition.getUseTunnels();
                maze = Maze.generateRandomShapedRoom(width, height, digPercentage, useTunnels);
                terrainGrid = createGridFrom(maze);
                break;

            default:
                throw new TarotRLException("Unhandled MapType encountered: " + definition.getMapType());
        }
        GameMap gameMap = new GameMap(mapName, terrainGrid, height, width);

        Map<Character, GameTileDefinition> tileSprites = definition.createSpriteToTile();
        gameMap.setTileSprites(tileSprites);

        char backgroundSprite = definition.getBackgroundSprite();
        gameMap.setOutOfBoundsTile(backgroundSprite);

        return gameMap;
    }

    private char[][] createGridFrom(String[] lines) {
        int gridWidth = lines[0].length();
        int gridHeight = lines.length;

        char[][] characterGrid = new char[gridHeight][gridWidth];

        for (int y = 0; y < gridHeight; y ++) {
            String row = lines[y];
            int rowWidth = row.length();
            for (int x = 0; x < rowWidth; x ++) {
                char sprite = row.charAt(x);
                characterGrid[y][x] = sprite;
            }
        }
        return characterGrid;
    }

    private char[][] createGridFrom(Maze maze) {
        int gridWidth = maze.getWidth();
        int gridHeight = maze.getHeight();

        char[][] characterGrid = new char[gridHeight][gridWidth];

        for (int y = 0; y < gridHeight; y++) {
            for(int x = 0; x < gridWidth; x++) {
                byte mazeCell = maze.getCell(x, y);
                char character;
                if (mazeCell == Maze.PATH) {
                    character = TileType.PATH.getSprite();
                } else if (mazeCell == Maze.WALL) {
                    character = TileType.WALL.getSprite();
                } else {
                    character = TileType.EMPTY.getSprite();
                }
                characterGrid[y][x] = character;
            }
        }
        return characterGrid;
    }

    /**
     * Helper method to generateMapFrom a byte[][] of paths and walls.
     *
     * @return a 2d byte array.
     */
    private Maze generateCellularAutomataRoom(int width, int height, int iterations) {
        Maze maze = Maze.generateCellularAutomataRoom(width, height);
        maze.connectDisconnectedComponents();
        for (int i = 0; i < iterations; i++) {
            maze.cellularAutomataIteration();
            maze.connectDisconnectedComponents();
        }
        return maze;
    }
}
