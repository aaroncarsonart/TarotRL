package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.game.GameMode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

/**
 * Handle user input for the associated {@link GameMode}. Must be registered
 * with a {@link com.aaroncarsonart.tarotrl.game.GameModeManager} to see
 * use in the game.
 * <p>
 * In general, use only one of {@link #handleKeyTyped(KeyboardEvent, UserInput)}
 * or the more granular {@link #handleKeyPressed(KeyboardEvent, UserInput)}
 * and {@link #handleKeyReleased(KeyboardEvent, UserInput)}, as sometimes
 * inputs can clash with one another.
 */
public interface InputHandler {
    UIEventResponse handleKeyPressed(KeyboardEvent keyboardEvent, UserInput userInput);

    UIEventResponse handleKeyReleased(KeyboardEvent keyboardEvent, UserInput userInput);

    UIEventResponse handleKeyTyped(KeyboardEvent keyboardEvent, UserInput userInput);
}

