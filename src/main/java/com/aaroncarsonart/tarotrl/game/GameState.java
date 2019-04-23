package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.menu.InventoryMenuData;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.inventory.GameItem;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    private static final Logger LOG = new Logger(GameState.class);

    private Position2D playerPosition;
    private Position3D inspectedPosition;

    private int turnCounter;
    private GameMap activeMap;
    private GameWorld gameWorld;

    private int stepCount;
    private int treasure;

    private PlayerAction previousAction = PlayerAction.UNKNOWN;
    private PlayerAction currentAction = PlayerAction.UNKNOWN;

    private List<GameItem> playerItems = new ArrayList<>();
    private TileType undefinedTileType;

    private List<String> statusLog = new ArrayList<>();
    private String status;

    private boolean gameOver;
    private boolean devMode;
    private boolean shiftDown;
    private boolean autoCollect;

    private boolean updateGameControllerAgain;

    private transient GameMode gameMode;
    private transient UserInput userInput = new UserInput();

    private InventoryMenuData inventoryMenuData;

    /**
     * Default no-arg constructor
     */
    public GameState() {
        inventoryMenuData = new InventoryMenuData();
        inventoryMenuData.setCancelAction(() -> setGameMode(GameMode.MAP_NAVIGATION));
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

    public Position3D getInspectedPosition() {
        return inspectedPosition;
    }

    public void setInspectedPosition(Position3D inspectedPosition) {
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
            LOG.info(status);
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

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void incrementStepCount() {
        this.stepCount += 1;
    }

    public int getTreasure() {
        return treasure;
    }

    public void gainTreasure(int amount) {
        this.treasure += amount;
    }

    public TileType getUndefinedTileType() {
        return undefinedTileType;
    }

    public void setUndefinedTileType(TileType undefinedTileType) {
        this.undefinedTileType = undefinedTileType;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }

    public List<GameItem> getPlayerItems() {
        return playerItems;
    }

    public void setPlayerItems(List<GameItem> playerItems) {
        this.playerItems = playerItems;
    }

    public void toggleAutoCollect() {
        autoCollect = !autoCollect;
    }

    public boolean isAutoCollectMode() {
        return autoCollect;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public GameMode getActiveGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public InventoryMenuData getInventoryMenuData() {
        return inventoryMenuData;
    }

    public void setInventoryMenuData(InventoryMenuData inventoryMenuData) {
        this.inventoryMenuData = inventoryMenuData;
    }

    public void setRepeatControllerUpdate(boolean repeatControllerUpdate) {
        this.updateGameControllerAgain = repeatControllerUpdate;
    }

    public boolean repeatControllerUpdate() {
        return updateGameControllerAgain;
    }
}
