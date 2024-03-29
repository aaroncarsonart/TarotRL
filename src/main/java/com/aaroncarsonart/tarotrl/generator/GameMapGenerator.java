package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameMap2D;
import com.aaroncarsonart.tarotrl.map.MapType;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;

/**
 * Encapsulates logic for Generating GameMaps from GameMapDefinitions.
 */
public class GameMapGenerator {
    private static final Logger LOG = new Logger(GameWorldGenerator.class);

    /**
     * This is the main outward facing interface of this class.
     *
     * @param definition The GameMapDefinition to generateMapFrom the GameMap2D from.
     * @return The generated GameMap2D that is defined by the GameMapDefinition.
     */
    public GameMap2D generateMapFrom(GameMapDefinition definition) {
        LOG.debug("generate GameMap2D from definition: \"%s\"", definition.getMapName());

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
                String errorMessage = "Unhandled MapType encountered: " + definition.getMapType();
                LOG.error(errorMessage);
                throw new TarotRLException(errorMessage);
        }
        GameMap2D gameMap = new GameMap2D(mapName, terrainGrid, height, width);
        return gameMap;
    }

    private static char[][] createGridFrom(String[] lines) {
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

    private static char[][] createGridFrom(Maze maze) {
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

    public static GameMap2D generateMapFrom(Maze maze) {
        char[][] grid = createGridFrom(maze);
        return new GameMap2D("Maze", grid, maze.getHeight(), maze.getWidth());
    }


    /**
     * Helper method to generateMapFrom a byte[][] of paths and walls.
     *
     * @return a 2d byte array.
     */
    public static Maze generateCellularAutomataRoom(int width, int height, int iterations) {
        Maze maze = Maze.generateCellularAutomataRoom(width, height);
        maze.connectDisconnectedComponents();
        for (int i = 0; i < iterations; i++) {
            maze.cellularAutomataIteration();
            maze.connectDisconnectedComponents();
        }
        return maze;
    }

    public static void main(String[] args) {
        GameMapDefinition definition = new GameMapDefinition();
        definition.setWidth(15);
        definition.setHeight(15);
        definition.setMapName("Test");

        GameMapGenerator generator = new GameMapGenerator();
        GameMap map;

        definition.setMapType(MapType.CELLULAR_AUTOMATA);
        definition.setIterations(3);
        map = generator.generateMapFrom(definition);
        System.out.println(map.toString());

        definition.setMapType(MapType.MAZE);
        map = generator.generateMapFrom(definition);
        System.out.println(map.toString());

        definition.setMapType(MapType.RANDOM);
        definition.setDigPercentage(0.2);
        definition.setUseTunnels(true);
        map = generator.generateMapFrom(definition);
        System.out.println(map.toString());
    }
}
