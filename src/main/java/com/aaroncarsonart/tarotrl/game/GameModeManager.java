package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.input.InputHandler;

import java.util.EnumMap;
import java.util.Set;

public class GameModeManager {
    private EnumMap<GameMode, GameModeComponents> componentsMap = new EnumMap<>(GameMode.class);

    public void registerComponents(GameMode gameMode, GameModeComponents components) {
        componentsMap.put(gameMode, components);
    }

    public Set<GameMode> getRegisteredModes() {
        return componentsMap.keySet();
    }

    public InputHandler getInputHandler(GameMode gameMode) {
        GameModeComponents components = componentsMap.get(gameMode);
        return components.getInputHandler();
    }

    public GameController getGameController(GameMode gameMode) {
        GameModeComponents components = componentsMap.get(gameMode);
        return components.getGameController();
    }

    public TileRenderer getTileRenderer(GameMode gameMode) {
        GameModeComponents components = componentsMap.get(gameMode);
        return components.getTileRenderer();
    }
}
