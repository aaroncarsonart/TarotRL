package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameSprite;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents one frame of a game's given state.
 */

public class GameState implements Serializable {

    // TODO refactor to use Map<String, GameMap> with the active map given by the key.
    // TODO have GameMaps reference one another by warp portals.
    // TODO warp portals can be one-directional or bi-directional, depending on the flavor text.

    private Map<String, GameMap> gameMaps = new HashMap<>();

    // Player info.
    private int playerPosY;
    private int playerPosX;

    private int turnCounter;

    @JsonIgnore
    private GameMap activeMap;

    /**
     * Default no-arg constructor
     */
    public GameState() {
    }

    /**
     * Create a deep copy of this GameState.  Makes some assumptions about values of fields.
     * @return A deep copy of the GameState.
     */
    public GameState createDeepCopy() {
        GameState clonedState = new GameState();

        clonedState.playerPosY = this.getPlayerPosY();
        clonedState.playerPosX = this.getPlayerPosX();

        for (GameMap originalMap : gameMaps.values()) {
            GameMap clonedMap = originalMap.createDeepCopy();
            clonedState.addGameMap(clonedMap);
        }
        GameMap clonedActiveMap = clonedState.lookupGameMap(activeMap.getKey());
        clonedState.setActiveMap(clonedActiveMap);
        return clonedState;
    }

    // ------------------------------------------------------
    // Getters and Setters
    // ------------------------------------------------------

    public int getPlayerPosY() {
        return playerPosY;
    }

    public void setPlayerPosY(int playerPosY) {
        this.playerPosY = playerPosY;
    }

    public int getPlayerPosX() {
        return playerPosX;
    }

    public void setPlayerPosX(int playerPosX) {
        this.playerPosX = playerPosX;
    }

    public GameMap getActiveMap() {
        return activeMap;
    }

    public void setActiveMap(GameMap activeMap) {
        this.activeMap = activeMap;
    }

    public void incrementTurnCounter() {
        turnCounter++;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    // ------------------------------------------------------
    // Other Methods
    // ------------------------------------------------------

    public void addGameMap(GameMap gameMap) {
        if (gameMaps.containsKey(gameMap.getKey())) {
            throw new TarotRLException("Map '" + gameMap.getKey() + "' has non-unique key.");
        }
        gameMaps.put(gameMap.getKey(), gameMap);
    }

    public GameMap lookupGameMap(String key) {
        return gameMaps.get(key);
    }



    public char[][] getSpritedMapCopy() {
        // copy the map state
        char[][] mapCopy = activeMap.getTileGridCopy();

        // draw sprites on the map
        mapCopy[playerPosY][playerPosX] = GameSprite.PLAYER;

        return mapCopy;
    }

}
