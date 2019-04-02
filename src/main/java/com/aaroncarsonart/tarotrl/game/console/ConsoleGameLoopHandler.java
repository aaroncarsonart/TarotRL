package com.aaroncarsonart.tarotrl.game.console;

import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameSprite;
import com.aaroncarsonart.tarotrl.map.GameMapUtils;

/**
 * Contains Logic for handling the Game loop and updating the game's state.
 */
public class ConsoleGameLoopHandler {

    /**
     * Read the player's input from the terminal, and update the game state.
     * @param inputHandler  The keyboard input handler.
     * @param actionHandler The game action handler.
     * @param gameState     The persistent game state.
     */
    public void doReadPlayerInput(
            ConsoleInputHandler inputHandler,
            GameActionHandler actionHandler,
            GameState gameState) {

        // read next player input, handling unknown commands
        System.out.print("Next command: ");
        PlayerAction nextAction = inputHandler.nextAction();

        while (nextAction == PlayerAction.UNKNOWN) {
            System.out.println("Unknown action.");

            System.out.print("Next command: ");
            nextAction = inputHandler.nextAction();
        }

        // pass command to the action handler, updating the game state
        actionHandler.processPlayerAction(nextAction, gameState);
    }

    /**
     * for now, draw the entire map wih the '@' sign representing the character.
     * Get more clever later.
     */
    public void doDrawGameMapState(GameState state) {
        GameMap map = state.getActiveMap();
        char[][] tileGrid = map.getTileGridCopy();

        int py = state.getPlayerPosY();
        int px = state.getPlayerPosX();
        tileGrid[py][px] = GameSprite.PLAYER;

        GameMapUtils.printMap2D(tileGrid, map.getHeight(), map.getWidth());
    }

}
