package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException;
import com.aaroncarsonart.tarotrl.map.MapType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
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

    @Test
    void testLoadDefinition_invalidConstraints() {
        loader.enableValidation();
        try {
            definition = loader.loadDefinition("/maps/invalid.json", GameMapDefinition.class);
            fail("expected ValidatedDefinitionException for invalid map JSON definition.");
        } catch (ValidatedDefinitionException e) {
            System.out.println(e.toString());
            List<String> validationErrors = e.getValidationErrors();
            assertNotNull(validationErrors, "Expected validationErrors to not be null.");
            assertEquals(2, validationErrors.size(), "Expected validationErrors to be present.");
            assertTrue(validationErrors.contains("mapName must not be null"));
            assertTrue(validationErrors.contains("mapType must not be null"));
        }
    }

    @Test
    void testLoadDefinition_invalidVault() {
        loader.enableValidation();
        try {
            definition = loader.loadDefinition("/maps/invalid_vault.json", GameMapDefinition.class);
            fail("expected ValidatedDefinitionException for invalid map JSON definition.");
        } catch (ValidatedDefinitionException e) {
            System.out.println(e.toString());
            List<String> validationErrors = e.getValidationErrors();
            assertNotNull(validationErrors, "Expected validationErrors to not be null.");
            assertEquals(3, validationErrors.size(), "Expected validationErrors to be present.");
            assertThat(validationErrors.get(0), startsWith("VAULT mapTypes must define the mapTerrainData"));
            assertThat(validationErrors.get(1), startsWith("VAULT mapTypes must not define a width"));
            assertThat(validationErrors.get(2), startsWith("VAULT mapTypes must not define a height"));
        }
    }

    @Test
    void testLoadDefinition_invalidVault_malformedTerrainData() {
        loader.enableValidation();
        try {
            definition = loader.loadDefinition("/maps/invalid_vault-malformed_terrain_data.json",
                    GameMapDefinition.class);
            fail("expected ValidatedDefinitionException for invalid map JSON definition.");
        } catch (ValidatedDefinitionException e) {
            System.out.println(e.toString());
            List<String> validationErrors = e.getValidationErrors();
            assertNotNull(validationErrors, "Expected validationErrors to not be null.");
            assertEquals(1, validationErrors.size(), "Expected validationErrors to be present.");
            assertThat(validationErrors.get(0), equalTo("VAULT mapTypes must define the mapTerrainData " +
                    "as an array of strings with even length (forming a rectangle)."));
        }
    }

    @Test
    void testLoadDefinition_invalidMaze() {
        loader.enableValidation();
        try {
            definition = loader.loadDefinition("/maps/invalid_maze.json", GameMapDefinition.class);
            fail("expected ValidatedDefinitionException for invalid map JSON definition.");
        } catch (ValidatedDefinitionException e) {
            System.out.println(e.toString());
            List<String> validationErrors = e.getValidationErrors();
            assertNotNull(validationErrors, "Expected validationErrors to not be null.");
            assertEquals(3, validationErrors.size(), "Expected validationErrors to be present.");
            assertThat(validationErrors.get(0), startsWith("MAZE mapTypes must not define the mapTerrainData"));
            assertThat(validationErrors.get(1), startsWith("MAZE mapTypes must define a width"));
            assertThat(validationErrors.get(2), startsWith("MAZE mapTypes must define a height"));
        }
    }

    @Test
    void testLoadDefinition_invalidCellularAutomata() {
        loader.enableValidation();
        try {
            definition = loader.loadDefinition("/maps/invalid_cellular_automata.json", GameMapDefinition.class);
            fail("expected ValidatedDefinitionException for invalid map JSON definition.");
        } catch (ValidatedDefinitionException e) {
            System.out.println(e.toString());
            List<String> validationErrors = e.getValidationErrors();
            assertNotNull(validationErrors, "Expected validationErrors to not be null.");
            assertEquals(1, validationErrors.size(), "Expected validationErrors to be present.");
            assertThat(validationErrors.get(0), startsWith("CELLULAR_AUTOMATA mapTypes must define the iterations field"));
        }
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
