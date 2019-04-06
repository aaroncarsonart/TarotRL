package com.aaroncarsonart.tarotrl.input;

import org.hexworks.zircon.api.input.KeyStroke;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handle user input from Zircon API.  Convert into PlayerActions
 * or other commands that update specifically the GameState.
 */
public class InputHandler {

    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();

    private PlayerAction nextAction = PlayerAction.UNKNOWN;

    private static Map<String, PlayerAction> initInputActionsMap() {
        Map<String, PlayerAction> inputActionsMap = new LinkedHashMap<>();
        inputActionsMap.put("?", PlayerAction.HELP);
        inputActionsMap.put("h", PlayerAction.HELP);

        inputActionsMap.put("q", PlayerAction.QUIT);

//        inputActionsMap.put("w", PlayerAction.MOVE_UP);
//        inputActionsMap.put("s", PlayerAction.MOVE_DOWN);
//        inputActionsMap.put("a", PlayerAction.MOVE_LEFT);
//        inputActionsMap.put("d", PlayerAction.MOVE_RIGHT);

        inputActionsMap.put("d", PlayerAction.DOOR);

        inputActionsMap.put("x", PlayerAction.CONFIRM);
        inputActionsMap.put("c", PlayerAction.CANCEL);

        inputActionsMap.put("up", PlayerAction.MOVE_UP);
        inputActionsMap.put("down", PlayerAction.MOVE_DOWN);
        inputActionsMap.put("left", PlayerAction.MOVE_LEFT);
        inputActionsMap.put("right", PlayerAction.MOVE_RIGHT);

        return inputActionsMap;
    }

    /**
     * Compute the next action to take, only if an existing action isn't
     * already queued up waiting to be consumed.
     * @param keyStroke The KeyStroke to be consumed.
     */
    public void handleKeyStroke(KeyStroke keyStroke) {
//        System.out.println(keyStroke);
        if (nextAction == PlayerAction.UNKNOWN) {
            nextAction = computeNextAction(keyStroke);
        }
    }

    private synchronized PlayerAction computeNextAction(KeyStroke keyStroke) {
        switch (keyStroke.inputType()) {
            case ArrowUp:    return PlayerAction.MOVE_UP;
            case ArrowDown:  return PlayerAction.MOVE_DOWN;
            case ArrowLeft:  return PlayerAction.MOVE_LEFT;
            case ArrowRight: return PlayerAction.MOVE_RIGHT;
            case Space:      return PlayerAction.REST;
            case Enter:  return PlayerAction.CONFIRM;
            case Escape: return PlayerAction.CANCEL;
        }
        char key = keyStroke.getCharacter();
        String keyStr = String.valueOf(key);
        PlayerAction nextAction = inputActionsMap.getOrDefault(keyStr, PlayerAction.UNKNOWN);
        return nextAction;
    }

    public synchronized PlayerAction consumeNextAction() {
        PlayerAction consumedAction = nextAction;
        nextAction = PlayerAction.UNKNOWN;
        return consumedAction;
    }
}
