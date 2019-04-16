package com.aaroncarsonart.tarotrl.game.controller;

/**
 * GameControllers drive game Functionality, when combined with their
 * associated InputHandler.
 */

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.UserInput;

/**
 * Responds to user input and drives game functionality. Must be registered
 * with a {@link com.aaroncarsonart.tarotrl.game.GameModeManager} to see
 * use in the game.
 */

public interface GameController {

    /**
     * Run one iteration of updating the entire GameState.
     * @param state The GameState to update.
     * @param input The UserInput to drive updating game components the player controls.
     * @return True, if the GameState was modified in a meaningful way such that the UI
     *         should be redrawn to reflect these changes.  Otherwise, false.
     */
    boolean update(GameState state, UserInput input);
}
