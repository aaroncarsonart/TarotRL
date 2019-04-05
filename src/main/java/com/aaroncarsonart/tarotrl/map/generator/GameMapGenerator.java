package com.aaroncarsonart.tarotrl.map.generator;

import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;

/**
 * Encapsulates logic for Generating GameMaps from GameMapDefinitions.
 */
public class GameMapGenerator {

    /**
     * TODO: implement!
     *
     * This is the main outward facing interface of this class.
     *
     * @param definition The GameMapDefinition to generate the GameMap from.
     * @return The generated GameMap that is defined by the GameMapDefinition.
     */
    public GameMap generate(GameMapDefinition definition) {
        String name = definition.getMapName();
        GameMap gameMap = null;
        return gameMap;
    }
}
