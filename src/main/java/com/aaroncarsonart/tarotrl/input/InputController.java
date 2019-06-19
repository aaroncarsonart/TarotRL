package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameModeManager;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.MouseEvent;
import org.hexworks.zircon.api.uievent.MouseEventType;
import org.hexworks.zircon.api.uievent.UIEventResponse;
import org.hexworks.zircon.api.uievent.UIEventSource;

import java.util.EnumMap;
import java.util.Map;

/**
 * Delegates the handling of user input from the {@link UIEventSource} to the proper
 * {@link InputHandler}, based on the active {@link GameMode}.
 */
public class InputController {
    private static final Logger LOG = new Logger(InputController.class);

    private Map<GameMode, InputHandler> inputHandlers = new EnumMap<>(GameMode.class);
    private UserInput userInput = new UserInput();

    public void registerInputHandlers(GameModeManager gameModeManager) {
        for (GameMode gameMode : gameModeManager.getRegisteredModes()) {
            InputHandler inputHandler = gameModeManager.getInputHandler(gameMode);
            inputHandlers.put(gameMode, inputHandler);
        }
    }

//    private InputHandler getActiveInputHandler(GameState state) {
//        GameMode gameMode = state.getActiveGameMode();
//        return inputHandlers.get(gameMode);
//    }

    public void listenForInputs(UIEventSource eventSource, GameState state) {
//        eventSource.onKeyboardEvent(KeyboardEventType.KEY_TYPED, (event, phase) -> handleKeyTyped(event, state));
//        eventSource.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> handleKeyPressed(event, state));
//        eventSource.onKeyboardEvent(KeyboardEventType.KEY_RELEASED, (event, phase) -> handleKeyReleased(event, state));

        KeyboardEventType keyTyped = KeyboardEventType.KEY_TYPED;
        KeyboardEventType keyPressed = KeyboardEventType.KEY_PRESSED;
        KeyboardEventType keyReleased = KeyboardEventType.KEY_RELEASED;

        eventSource.processKeyboardEvents(keyTyped, (event, phase) -> handleKeyboardEvent(keyTyped, event, state));
        eventSource.processKeyboardEvents(keyPressed, (event, phase) -> handleKeyboardEvent(keyPressed, event, state));
        eventSource.processKeyboardEvents(keyReleased, (event, phase) -> handleKeyboardEvent(keyReleased, event, state));
        eventSource.processMouseEvents(MouseEventType.MOUSE_CLICKED, (event, phase) -> handleMouseEvent(MouseEventType.MOUSE_CLICKED, event, state));
    }

//    private UIEventResponse handleKeyTyped(KeyboardEvent event, GameState state) {
//        GameMode activeGameMode = state.getActiveGameMode();
//        InputHandler inputHandler = inputHandlers.get(activeGameMode);
//        return inputHandler.handleKeyTyped(event, state);
//    }
//
//    private UIEventResponse handleKeyPressed(KeyboardEvent event, GameState state) {
//        GameMode activeGameMode = state.getActiveGameMode();
//        InputHandler inputHandler = inputHandlers.get(activeGameMode);
//        return inputHandler.handleKeyPressed(event, state);
//    }
//
//    private UIEventResponse handleKeyReleased(KeyboardEvent event, GameState state) {
//        GameMode activeGameMode = state.getActiveGameMode();
//        InputHandler inputHandler = inputHandlers.get(activeGameMode);
//        return inputHandler.handleKeyReleased(event, state);
//    }

    private  UIEventResponse handleKeyboardEvent(KeyboardEventType type, KeyboardEvent event, GameState state) {
        GameMode activeGameMode = state.getActiveGameMode();
        InputHandler inputHandler = inputHandlers.get(activeGameMode);
        switch (type) {
            case KEY_TYPED:    return inputHandler.handleKeyTyped(event, userInput);
            case KEY_PRESSED:  return inputHandler.handleKeyPressed(event, userInput);
            case KEY_RELEASED: return inputHandler.handleKeyReleased(event, userInput);
        }
        return UIEventResponses.pass();
    }

    private  UIEventResponse handleMouseEvent(MouseEventType type, MouseEvent event, GameState state) {
        LOG.info(event);
        GameMode activeGameMode = state.getActiveGameMode();
        InputHandler inputHandler = inputHandlers.get(activeGameMode);
//        switch (type) {
//            case MOUSE_MOVED:   return inputHandler.handleKeyTyped(event, userInput);
//        }
        return UIEventResponses.pass();
    }



    public UserInput getUserInput() {
        return userInput;
    }

    public Object acquireInputLock() {
        return userInput.getLock();
    }
}
