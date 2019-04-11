package com.aaroncarsonart.tarotrl.map.generator;

import com.aaroncarsonart.tarotrl.util.Globals;
import com.aaroncarsonart.tarotrl.generator.GameMapGenerator;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.MapType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameMapGeneratorTest {
    private static final Logger LOG = new Logger(GameMapGeneratorTest.class);

    // The answer to life, the universe, and everything ... and also *
    private static final long RANDOM_SEED = 42L;

    private static final String VAULT_NAME = "Vault definition for map generator test";
    private static final int VAULT_WIDTH = 7;
    private static final int VAULT_HEIGHT = 5;
    private static final String[] VAULT_DATA_STRING_ARRAY = new String[]{
            "#.. ..#",
            " #...# ",
            "  #.#  ",
            " #...# ",
            "#.. ..#"
    };
    private static final char[][] VAULT_DATA_2D_CHAR_ARRAY = new char[][]{
            { '#', '.', '.', ' ', '.', '.', '#' },
            { ' ', '#', '.', '.', '.', '#', ' ' },
            { ' ', ' ', '#', '.', '#', ' ', ' ' },
            { ' ', '#', '.', '.', '.', '#', ' ' },
            { '#', '.', '.', ' ', '.', '.', '#' }
    };

    private static final String MAZE_NAME = "Maze definition for map generator test";
    private static final int MAZE_WIDTH = 5;
    private static final int MAZE_HEIGHT = 7;
    private static final char[][] MAZE_DATA_2D_CHAR_ARRAY = new char[][]{
            { '#', '#', '#', '#', '#' },
            { '#', '.', '.', '.', '#' },
            { '#', '#', '#', '.', '#' },
            { '#', '.', '.', '.', '#' },
            { '#', '.', '#', '#', '#' },
            { '#', '.', '.', '.', '#' },
            { '#', '#', '#', '#', '#' }
    };

    private static final String CELLULAR_AUTOMATA_NAME = "Maze definition for map generator test";
    private static final int CELLULAR_AUTOMATA_WIDTH = 10;
    private static final int CELLULAR_AUTOMATA_HEIGHT = 8;
    private static final int CELLULAR_AUTOMATA_ITERATIONS = 0;

    private static final char[][] CELLULAR_AUTOMATA_2D_CHAR_ARRAY = new char[][]{
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            { '#', '#', '#', '#', '.', '.', '.', '#', '#', '#' },
            { '#', '#', '#', '.', '.', '.', '.', '.', '#', '#' },
            { '#', '#', '.', '.', '.', '.', '.', '.', '#', '#' },
            { '#', '#', '#', '.', '.', '.', '.', '#', '#', '#' },
            { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' }
};

    // Just test the two classes in tandem, as that's how they'll be used.
//    JsonDefinitionLoader loader;
    GameMapGenerator generator;

    @BeforeEach
    void before() {
        generator = new GameMapGenerator();
        Globals.RANDOM.setSeed(RANDOM_SEED);
    }

    @Test
    void testGenerateVaultMap() throws Exception {
        GameMapDefinition vaultMapDefinition = getVaultMapDefinition();
        GameMap vaultGameMap = generator.generateMapFrom(vaultMapDefinition);
        LOG.testing(vaultGameMap);

        assertEquals(VAULT_NAME, vaultGameMap.getName());
        assertEquals(VAULT_WIDTH, vaultGameMap.getWidth());
        assertEquals(VAULT_HEIGHT, vaultGameMap.getHeight());
        assertArrayEquals(VAULT_DATA_2D_CHAR_ARRAY, vaultGameMap.getTileGridCopy());
    }

    private GameMapDefinition getVaultMapDefinition() {
        GameMapDefinition definition = new GameMapDefinition();
        definition.setMapName(VAULT_NAME);
        definition.setMapType(MapType.VAULT);
        definition.setWidth(VAULT_WIDTH);
        definition.setHeight(VAULT_HEIGHT);
        definition.setMapTerrainData(VAULT_DATA_STRING_ARRAY);
        return definition;
    }

    @Test
    void testGenerateMazeMap() throws Exception {
        GameMapDefinition mazeMapDefinition = getMazeMapDefinition();
        GameMap mazeGameMap = generator.generateMapFrom(mazeMapDefinition);
        LOG.testing(mazeGameMap);

        assertEquals(MAZE_NAME, mazeGameMap.getName());
        assertEquals(MAZE_WIDTH, mazeGameMap.getWidth());
        assertEquals(MAZE_HEIGHT, mazeGameMap.getHeight());
        assertArrayEquals(MAZE_DATA_2D_CHAR_ARRAY, mazeGameMap.getTileGridCopy());
    }

    private GameMapDefinition getMazeMapDefinition() {
        GameMapDefinition definition = new GameMapDefinition();
        definition.setMapName(MAZE_NAME);
        definition.setMapType(MapType.MAZE);
        definition.setWidth(MAZE_WIDTH);
        definition.setHeight(MAZE_HEIGHT);
        definition.setMapTerrainData(null);
        return definition;
    }


    @Test
    void testGenerateCellularAutomataMap() throws Exception {
        GameMapDefinition cellularAutomataMapDefinition = getCellularAutomataMapDefinition();
        GameMap cellularAutomataGameMap = generator.generateMapFrom(cellularAutomataMapDefinition);
        LOG.testing(cellularAutomataGameMap);

        assertEquals(CELLULAR_AUTOMATA_NAME, cellularAutomataGameMap.getName());
        assertEquals(CELLULAR_AUTOMATA_WIDTH, cellularAutomataGameMap.getWidth());
        assertEquals(CELLULAR_AUTOMATA_HEIGHT, cellularAutomataGameMap.getHeight());
        assertArrayEquals(CELLULAR_AUTOMATA_2D_CHAR_ARRAY, cellularAutomataGameMap.getTileGridCopy());
    }

    private GameMapDefinition getCellularAutomataMapDefinition() {
        GameMapDefinition definition = new GameMapDefinition();
        definition.setMapName(CELLULAR_AUTOMATA_NAME);
        definition.setMapType(MapType.CELLULAR_AUTOMATA);
        definition.setWidth(CELLULAR_AUTOMATA_WIDTH);
        definition.setHeight(CELLULAR_AUTOMATA_HEIGHT);
        definition.setMapTerrainData(null);
        definition.setIterations(CELLULAR_AUTOMATA_ITERATIONS);
        return definition;
    }
}
