package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.map.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encloses all game data that needs to be
 * defined within a JSON resource file.
 *
 * All classes used and referenced by GameDataDefinition
 * that are not marked with JsonIgnore should either be
 * custom classes defined explicitly within this same package,
 * or be simple enum, struct, or POJO style classes with no
 * special behavior or functions.
 */
public class GameDataDefinition {

    List<GameMap> gameMaps = new ArrayList<>();

    public List<GameMap> getGameMaps() {
        return gameMaps;
    }

    public void setGameMaps(List<GameMap> gameMaps) {
        this.gameMaps = gameMaps;
    }
}
