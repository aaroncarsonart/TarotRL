package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.input.InputHandler;
import com.aaroncarsonart.tarotrl.util.Logger;

public enum GameMode {
    MAP_NAVIGATION,
    SNAPSHOT_HISTORY,
    INVENTORY;

    private static Logger LOG = new Logger(GameMode.class);
    private GameModeComponents components;

    public void registerComponents(GameModeComponents components) {
        if (this.components == null) {
            this.components = components;
        } else {
            String errorMessage = "Error with " + this + ": only register GameMode components once.";
            LOG.error(errorMessage);
            throw new TarotRLException(errorMessage);
        }
    }

//    public static void registerComponents(GameMode gameMode, GameModeComponents components) {
//        gameMode.components = components;
//    }

    public InputHandler getInputHandler() {
        return components.getInputHandler();
    }

    public GameController getGameController() {
        return components.getGameController();
    }

    public TileRenderer getTileRenderer() {
        return components.getTileRenderer();
    }
}
