package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents one frame of a game's given state.
 */

public class GameState implements Serializable {
    private Map<String, GameMap> gameMaps = new HashMap<>();

    // Player info.
    private Position2D playerPosition;
    private Position2D inspectedPosition;

    private int turnCounter;
    private GameMap activeMap;

    private PlayerAction previousAction = PlayerAction.UNKNOWN;
    private PlayerAction currentAction = PlayerAction.UNKNOWN;

    private List<String> statusLog = new ArrayList<>();
    private String status;

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

    public Position2D getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position2D playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Position2D getInspectedPosition() {
        return inspectedPosition;
    }

    public void setInspectedPosition(Position2D inspectedPosition) {
        this.inspectedPosition = inspectedPosition;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (StringUtils.isNotBlank(status)) {
            this.statusLog.add(status);
        }
    }

    public List<String> getStatusLog() {
        return statusLog;
    }

    public PlayerAction getPreviousAction() {
        return previousAction;
    }

    public void setPreviousAction(PlayerAction previousAction) {
        this.previousAction = previousAction;
    }

    public PlayerAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(PlayerAction currentAction) {
        this.currentAction = currentAction;
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
}
