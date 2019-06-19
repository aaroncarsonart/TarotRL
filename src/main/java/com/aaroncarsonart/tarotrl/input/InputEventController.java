package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.graphics.SnapshotHistory;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.UIEventResponse;
import org.hexworks.zircon.api.uievent.UIEventSource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handle user input from Zircon API.  Convert user input
 * (key presses, mouse actions, etc) into a standardized
 * UserInput object, that is then interpreted by the
 * PlayerActionController.
 */
public class InputEventController {
    private static final Logger LOG = new Logger(InputEventController.class);
    private static final GameMode SUPPORTED_GAME_MODE = GameMode.MAP_NAVIGATION;

    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();
    private PlayerActionEmitter playerActionEmitter = new PlayerActionEmitter();

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

    public void listenForInputs(TileGrid grid, GameState state) {
        grid.processKeyboardEvents(KeyboardEventType.KEY_TYPED, (event, phase) -> handleKeyTyped(event, state));
        grid.processKeyboardEvents(KeyboardEventType.KEY_PRESSED, (event, phase) -> handleKeyPressed(event, state));
        grid.processKeyboardEvents(KeyboardEventType.KEY_RELEASED, (event, phase) -> handleKeyReleased(event, state));
    }

    public void addPlayerActionListener(PlayerActionListener listener) {
        playerActionEmitter.addListener(listener);
    }

    private UIEventResponse handleKeyTyped(KeyboardEvent event, GameState gameState) {
        if (gameState.getActiveGameMode() != SUPPORTED_GAME_MODE) {
            return UIEventResponses.pass();
        }
        LOG.debug("handleKeyTyped(): " + event);

        PlayerAction nextAction = inputActionsMap.getOrDefault(event.getKey(), PlayerAction.UNKNOWN);
        playerActionEmitter.broadcastPlayerAction(nextAction);
        return UIEventResponses.processed();
    }

    private UIEventResponse handleKeyPressed(KeyboardEvent event, GameState gameState) {
        if (gameState.getActiveGameMode() != SUPPORTED_GAME_MODE) {
            return UIEventResponses.pass();
        }
        LOG.debug("handleKeyPressed(): " + event);

        if (event.getKey().equals(SnapshotHistory.KEY_NEXT_SNAPSHOT) ||
                event.getKey().equals(SnapshotHistory.KEY_PREVIOUS_SNAPSHOT)) {
            gameState.setGameMode(GameMode.SNAPSHOT_HISTORY);
            return UIEventResponses.processed();
        }


        toggleBooleanFlags(event);
        gameState.setShiftDown(shiftDown);
        gameState.setDevMode(devMode);

        PlayerAction nextAction = getPlayerActionFromKeyCode(event.getCode());
        playerActionEmitter.broadcastPlayerAction(nextAction);
        return UIEventResponses.processed();
    }

    private UIEventResponse handleKeyReleased(KeyboardEvent event, GameState gameState) {
        if (gameState.getActiveGameMode() != SUPPORTED_GAME_MODE) {
            return UIEventResponses.pass();
        }
        LOG.debug("handleKeyReleased(): " + event);

        toggleBooleanFlags(event);
        gameState.setShiftDown(shiftDown);
        gameState.setDevMode(devMode);

        playerActionEmitter.broadcastPlayerAction(PlayerAction.UNKNOWN);
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


    public void listenForInputs(UIEventSource eventSource) {

    }

    private UserInput playerInput = new UserInput();

    public UserInput getPlayerInput() {
        return playerInput;
    }
}
