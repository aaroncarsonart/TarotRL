package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException;
import com.aaroncarsonart.tarotrl.map.MapType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the behavior of loading JSON definition files from the resources folder.
 */
class JsonDefinitionLoaderTest {

    private JsonDefinitionLoader loader;
    private GameMapDefinition definition;

    @BeforeEach
    void before(){
        loader = new JsonDefinitionLoader();
    }

    @Test
    void testEnableValidation() {
        assertTrue(loader.getValidationEnabled(), "Validation should be enabled by default.");

        loader.disableValidation();
        assertFalse(loader.getValidationEnabled(),  "Validation should be disabled.");

        loader.enableValidation();
        assertTrue(loader.getValidationEnabled(), "Validation should be disabled.");
    }

    /**
     * Test the expected VAULT mapType behavior.
     * @throws Exception If a {@link ValidatedDefinitionException} occurs.
     */
    @Test
    void testLoadDefinition_vault() throws Exception {
        definition = loader.loadDefinition("/maps/valid_vault.json", GameMapDefinition.class);

        // pass validation (no exceptions thrown)

        String expectedMapName = "A valid vault map definition file";
        MapType expectedMapType = MapType.VAULT;

        assertEquals(expectedMapName, definition.getMapName(), "unexpected value loaded from JSON");
        assertEquals(expectedMapType, definition.getMapType(), "unexpected value loaded from JSON");
        assertNull(definition.getWidth(), "unexpected value loaded from JSON");
        assertNull(definition.getHeight(), "unexpected value loaded from JSON");

        assertNotNull(definition.getMapTerrainData(), "unexpected value loaded from JSON");

        for (String row : definition.getMapTerrainData()) {
            System.out.println(row);
        }

        definition.normalize();

        assertEquals(expectedMapName, definition.getMapName(), "normalization error");
        assertEquals(expectedMapType, definition.getMapType(), "normalization error");
        assertEquals(17, definition.getWidth(), "normalization error");
        assertEquals(9, definition.getHeight(), "normalization error");

        assertNotNull(definition.getMapTerrainData(), "normalization error");
        assertNull(definition.getIterations(), "normalization error");
    }

    /**
     * Test the expected MAZE mapType behavior.
     * @throws Exception If a {@link ValidatedDefinitionException} occurs.
     */
    @Test
    void testLoadDefinition_maze() throws Exception {
        definition = loader.loadDefinition("/maps/valid_maze.json", GameMapDefinition.class);

        // pass validation (no exceptions thrown)

        String expectedMapName = "A valid maze map definition file";
        MapType expectedMapType = MapType.MAZE;

        assertEquals(expectedMapName, definition.getMapName(), "unexpected value loaded from JSON");
        assertEquals(expectedMapType, definition.getMapType(), "unexpected value loaded from JSON");
        assertEquals(27, definition.getWidth(), "unexpected value loaded from JSON");
        assertEquals(9, definition.getHeight(), "unexpected value loaded from JSON");

        assertNull(definition.getMapTerrainData(), "unexpected value loaded from JSON");
        assertNull(definition.getIterations(), "unexpected value loaded from JSON");

        definition.normalize();

        assertEquals(expectedMapName, definition.getMapName(), "normalization error");
        assertEquals(expectedMapType, definition.getMapType(), "normalization error");
        assertEquals(27, definition.getWidth(), "normalization error");
        assertEquals(9, definition.getHeight(), "normalization error");

        assertNull(definition.getMapTerrainData(), "normalization error");
        assertNull(definition.getIterations(), "normalization error");
    }

    @Test
    void testLoadDefinition_cellularAutomata() throws Exception {
        definition = loader.loadDefinition("/maps/valid_cellular_automata.json", GameMapDefinition.class);

        // pass validation (no exceptions thrown)

        String expectedMapName = "A valid cellular automata map definition file";
        MapType expectedMapType = MapType.CELLULAR_AUTOMATA;

        assertEquals(expectedMapName, definition.getMapName(), "unexpected value loaded from JSON");
        assertEquals(expectedMapType, definition.getMapType(), "unexpected value loaded from JSON");
        assertEquals(19, definition.getWidth(), "unexpected value loaded from JSON");
        assertEquals(13, definition.getHeight(), "unexpected value loaded from JSON");

        assertNull(definition.getMapTerrainData(), "unexpected value loaded from JSON");
        assertNotNull(definition.getIterations(), "unexpected value loaded from JSON");
        assertEquals(3, definition.getIterations(), "unexpected value loaded from JSON");

        definition.normalize();

        assertEquals(expectedMapName, definition.getMapName(), "normalization error");
        assertEquals(expectedMapType, definition.getMapType(), "normalization error");
        assertEquals(19, definition.getWidth(), "normalization error");
        assertEquals(13, definition.getHeight(), "normalization error");

        assertNull(definition.getMapTerrainData(), "normalization error");
        assertEquals(3, definition.getIterations(), "normalization error");
    }
}
