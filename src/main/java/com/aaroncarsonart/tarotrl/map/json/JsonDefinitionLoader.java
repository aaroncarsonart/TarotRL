package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.Globals;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException;
import com.aaroncarsonart.tarotrl.validation.DefinitionValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Contains Logic for loading GameMaps and other GameState entities
 * with definable, configurable behavior from JSON definition files.
 */
public class JsonDefinitionLoader {

    private ObjectMapper mapper  = Globals.OBJECT_MAPPER;
    private DefinitionValidator validator = new DefinitionValidator();
    private boolean validationEnabled = true;

    public <T> T loadDefinition(String path, Class<T> type) throws ValidatedDefinitionException {
        URL url = JsonDefinitionLoader.class.getResource(path);
        try {
            T obj = mapper.readValue(url, type);
            if (validationEnabled) {
                validator.doValidate(obj);
            }
            return obj;
        } catch (IOException e) {
            String message = "Error loading type " + type.getName() + " from path " + path;
            throw new TarotRLException(message, e);
        } catch (ValidatedDefinitionException e) {
            // Intercept the exception, and set the definition file path for quicker debugging.
            e.setDefinitionFilePath(path);
            throw e;
        }
    }

    /**
     * Validate, normalize, and return the GameMapDefinition described by
     * the JSON definition file located at the given path.
     * @param path The resource file path to load the GameMapDefinition from.
     * @return The validated, normalized GameMapDefinition described by
     *         the JSON definition file located at the given path.
     */
    public GameMapDefinition loadGameMapDefinition(String path) {
        try {
            GameMapDefinition definition = loadDefinition(path, GameMapDefinition.class);
            definition.normalize();
            return definition;
        } catch (ValidatedDefinitionException e) {
            throw new TarotRLException(e);
        }
    }

    public void enableValidation() {
        validationEnabled = true;
    }

    public void disableValidation() {
        validationEnabled = false;
    }

    public boolean getValidationEnabled() {
        return validationEnabled;
    }
}
