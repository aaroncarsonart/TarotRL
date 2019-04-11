package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.deck.TarotDeck;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException;
import com.aaroncarsonart.tarotrl.util.Globals;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.validation.DefinitionValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Contains Logic for loading GameMaps and other GameState entities
 * with definable, configurable behavior from JSON definition files.
 */
public class JsonDefinitionLoader {
    private static final Logger LOG = new Logger(JsonDefinitionLoader.class);
    private static final ObjectMapper mapper  = Globals.OBJECT_MAPPER;
    private static final DefinitionValidator validator = new DefinitionValidator();
    private boolean validationEnabled = true;

    public void enableValidation() {
        validationEnabled = true;
    }

    public void disableValidation() {
        validationEnabled = false;
    }

    public boolean getValidationEnabled() {
        return validationEnabled;
    }

    public <T> T loadDefinition(String path, Class<T> type) throws ValidatedDefinitionException {
        LOG.info("loadDefinition(path=\"%s\",type=\"%s\")", path, type);
        ClassLoader classLoader = JsonDefinitionLoader.class.getClassLoader();
        URL url = classLoader.getResource(path);
        try {
            T obj = mapper.readValue(url, type);
            if (validationEnabled) {
                validator.doValidate(obj);
            }
            return obj;
        } catch (IOException e) {
            String message = "Error loading type \"" + type + "\" from path: \"" + path + "\"";
            LOG.error(e);
            throw new TarotRLException(message, e);
        } catch (ValidatedDefinitionException e) {
            // Intercept the exception, and set the definition file path for quicker debugging.
            e.setDefinitionFilePath(path);
            LOG.error(e);
            throw e;
        }
    }

    /**
     * Load The deck deck from the config.
     * @return The deck deck.
     */
    public TarotDeck loadTarotDeck() {
        try {
        TarotDeck tarotDeck = loadDefinition("tarot_deck.json", TarotDeck.class);
            return tarotDeck;
          } catch (ValidatedDefinitionException e) {
            throw new TarotRLException(e);
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

    public GameConfigDefinition loadGameConfig(String path) {
        try {
            GameConfigDefinition config = loadDefinition(path, GameConfigDefinition.class);
            return config;
        } catch (ValidatedDefinitionException e) {
            throw new TarotRLException(e);
        }
    }
    public TileDefinitionSet loadTileDefinitionSet(String path) {
        try {
            TileDefinitionSet result = loadDefinition(path, TileDefinitionSet.class);
            return result;
        } catch (ValidatedDefinitionException e) {
            throw new TarotRLException(e);
        }
    }
}
