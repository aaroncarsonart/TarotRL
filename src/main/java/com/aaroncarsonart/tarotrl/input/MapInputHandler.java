package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapInputHandler implements InputHandler {
    private static final Logger LOG = new Logger(MapInputHandler.class);

    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();
//    private PlayerActionEmitter playerActionEmitter = new PlayerActionEmitter();

    private static Map<String, PlayerAction> initInputActionsMap() {
        Map<String, PlayerAction> inputActionsMap = new LinkedHashMap<>();
        inputActionsMap.put("?", PlayerAction.HELP);
        inputActionsMap.put("h", PlayerAction.HELP);

        inputActionsMap.put("q", PlayerAction.QUIT);

        inputActionsMap.put("d", PlayerAction.DOOR);
        inputActionsMap.put("p", PlayerAction.AUTO_PICKUP_ITEMS);

        inputActionsMap.put("x", PlayerAction.CONFIRM);
        inputActionsMap.put("c", PlayerAction.CANCEL);

        inputActionsMap.put("i", PlayerAction.INVENTORY);
        inputActionsMap.put("[", PlayerAction.SNAPSHOT_HISTORY);
        inputActionsMap.put("]", PlayerAction.SNAPSHOT_HISTORY);

        inputActionsMap.put("up", PlayerAction.MOVE_UP);
        inputActionsMap.put("down", PlayerAction.MOVE_DOWN);
        inputActionsMap.put("left", PlayerAction.MOVE_LEFT);
        inputActionsMap.put("right", PlayerAction.MOVE_RIGHT);

        return inputActionsMap;
    }

//    public void listenForInputs(TileGrid grid, GameState state) {
//        grid.onKeyboardEvent(KeyboardEventType.KEY_TYPED, (event, phase) -> handleKeyTyped(event, state));
//        grid.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> handleKeyPressed(event, state));
//        grid.onKeyboardEvent(KeyboardEventType.KEY_RELEASED, (event, phase) -> handleKeyReleased(event, state));
//    }

//    public void addPlayerActionListener(PlayerActionListener listener) {
//        playerActionEmitter.addListener(listener);
//    }

    @Override
    public UIEventResponse handleKeyTyped(KeyboardEvent event, UserInput input) {
//        LOG.debug("handleKeyTyped(): " + event);
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyPressed(KeyboardEvent event, UserInput input) {
//        LOG.debug("handleKeyPressed(): " + event);

        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.SHIFT) {
            input.enableModifier(MapActionModifier.SINGLE_STEP_MODE);
        }else if (keyCode == KeyCode.ALT) {
            input.enableModifier(MapActionModifier.DEV_MODE);
        }

        PlayerAction nextAction = getPlayerActionFromKeyCode(event.getCode());

        if (nextAction != PlayerAction.UNKNOWN) {
            input.setAction(nextAction);
            input.doUpdateController();
        } else {
            nextAction = inputActionsMap.getOrDefault(event.getKey(), PlayerAction.UNKNOWN);
            input.setAction(nextAction);
            input.doUpdateController();

        }

        return UIEventResponses.processed();
    }

    @Override
    public UIEventResponse handleKeyReleased(KeyboardEvent event, UserInput input) {
//        LOG.debug("handleKeyReleased(): " + event);

        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.SHIFT) {
            input.disableModifier(MapActionModifier.SINGLE_STEP_MODE);
        }else if (keyCode == KeyCode.ALT) {
            input.disableModifier(MapActionModifier.DEV_MODE);
        }

//        playerActionEmitter.broadcastPlayerAction(PlayerAction.UNKNOWN);
//        input.doUpdateController();

        return UIEventResponses.processed();
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
}
