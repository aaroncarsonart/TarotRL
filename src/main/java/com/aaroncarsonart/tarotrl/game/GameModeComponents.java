package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.input.InputHandler;

/**
 * To fully support a new {@link  GameMode}, the individual components for handling
 * user input, updating the {@link GameState}, and rendering the game must each
 * be implemented for the given mode.
 *
 * These components are grouped together here and then registered with the
 * {@link GameModeManager} for proper delegation during the game loop.
 */
public class GameModeComponents {
    private InputHandler inputHandler;
    private GameController gameController;
    private TileRenderer tileRenderer;

    public GameModeComponents(InputHandler inputHandler, GameController gameController, TileRenderer tileRenderer) {
        this.inputHandler = inputHandler;
        this.gameController = gameController;
        this.tileRenderer = tileRenderer;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public GameController getGameController() {
        return gameController;
    }

    public TileRenderer getTileRenderer() {
        return tileRenderer;
    }
}
