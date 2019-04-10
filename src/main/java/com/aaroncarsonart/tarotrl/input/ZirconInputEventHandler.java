package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.game.GameState;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.UIEventResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handle user input from Zircon API.  Convert into PlayerActions
 * or other commands that update specifically the GameState.
 *
 * Note: this only works with Zircon version 2018.12.25-XMAS.
 * for newer builds, use
 */
public class ZirconInputEventHandler {

    private boolean DEBUG = true;

    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();

    private PlayerAction nextAction = PlayerAction.UNKNOWN;
    private boolean devMode = false;
    private boolean shiftDown = false;

    private static Map<String, PlayerAction> initInputActionsMap() {
        Map<String, PlayerAction> inputActionsMap = new LinkedHashMap<>();
        inputActionsMap.put("?", PlayerAction.HELP);
        inputActionsMap.put("h", PlayerAction.HELP);

        inputActionsMap.put("q", PlayerAction.QUIT);

        inputActionsMap.put("d", PlayerAction.DOOR);
        inputActionsMap.put("p", PlayerAction.AUTO_PICKUP_ITEMS);

        inputActionsMap.put("x", PlayerAction.CONFIRM);
        inputActionsMap.put("c", PlayerAction.CANCEL);

        inputActionsMap.put("up", PlayerAction.MOVE_UP);
        inputActionsMap.put("down", PlayerAction.MOVE_DOWN);
        inputActionsMap.put("left", PlayerAction.MOVE_LEFT);
        inputActionsMap.put("right", PlayerAction.MOVE_RIGHT);

        return inputActionsMap;
    }

    public void listenForInput(TileGrid grid, GameState state) {
        grid.onKeyboardEvent(KeyboardEventType.KEY_TYPED, (event, phase) -> handleKeyTyped(event, state));
        grid.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> handleKeyPressed(event, state));
        grid.onKeyboardEvent(KeyboardEventType.KEY_RELEASED, (event, phase) -> handleKeyReleased(event, state));
    }

    @SuppressWarnings("unused")
    private UIEventResponse handleKeyTyped(KeyboardEvent event, GameState state) {
        if (DEBUG) System.out.println("handleKeyTyped(): " + event);
        nextAction = inputActionsMap.getOrDefault(event.getKey(), PlayerAction.UNKNOWN);
        return UIEventResponses.processed();
    }

    private UIEventResponse handleKeyPressed(KeyboardEvent event, GameState state) {
        if (DEBUG) System.out.println("handleKeyPressed(): " + event);
        nextAction = getPlayerActionFromKeyCode(event.getCode());

        toggleBooleanFlags(event);
        state.setShiftDown(shiftDown);
        state.setDevMode(devMode);
        return UIEventResponses.processed();
    }

    private UIEventResponse handleKeyReleased(KeyboardEvent event, GameState state) {
        if (DEBUG) System.out.println("handleKeyReleased(): " + event);
        toggleBooleanFlags(event);
        state.setShiftDown(shiftDown);
        state.setDevMode(devMode);
        return UIEventResponses.processed();
    }

    private void toggleBooleanFlags(KeyboardEvent event) {
        boolean isPressed = event.getType() == KeyboardEventType.KEY_PRESSED;
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.SHIFT) {
            shiftDown = isPressed;
        }else if (keyCode == KeyCode.ALT) {
            devMode = isPressed;
        }
    }

    private PlayerAction getPlayerActionFromKeyCode(KeyCode keyCode) {
        switch(keyCode) {
            case UP:     return PlayerAction.MOVE_UP;
            case DOWN:   return PlayerAction.MOVE_DOWN;
            case LEFT:   return PlayerAction.MOVE_LEFT;
            case RIGHT:  return PlayerAction.MOVE_RIGHT;
            case SPACE:  return PlayerAction.REST;
            case ENTER:  return PlayerAction.CONFIRM;
            case ESCAPE: return PlayerAction.CANCEL;
        }
        return PlayerAction.UNKNOWN;
    }

    public synchronized PlayerAction getNextAction() {
        return nextAction;
    }
    public synchronized void clearNextAction() {
        nextAction = PlayerAction.UNKNOWN;
    }

    public boolean getDevMove() {
        return devMode;
    }

    public boolean getShiftDown() {
        return shiftDown;
    }
}
