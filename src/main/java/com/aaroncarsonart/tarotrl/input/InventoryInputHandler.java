package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.menu.MenuAction;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

/**
 * Manage input handling for: {@link com.aaroncarsonart.tarotrl.game.GameMode#INVENTORY}.
 */
public class InventoryInputHandler implements InputHandler {
    private static final Logger LOG = new Logger(InventoryInputHandler.class);

    @Override
    public UIEventResponse handleKeyPressed(KeyboardEvent event, UserInput input) {
        LOG.info(event);
        if (Key.ENTER.hasEvent(event)) {
            input.setAction(MenuAction.OK);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.ESCAPE.hasEvent(event) || Key.BACK_SLASH.hasEvent(event)) {
            input.setAction(MenuAction.CANCEL);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.UP.hasEvent(event)) {
            input.setAction(MenuAction.UP);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.DOWN.hasEvent(event)) {
            input.setAction(MenuAction.DOWN);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.LEFT.hasEvent(event)) {
            input.setAction(MenuAction.LEFT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.RIGHT.hasEvent(event)) {
            input.setAction(MenuAction.RIGHT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.OPEN_BRACKET.hasEvent(event)) {
            input.setAction(MenuAction.PREVIOUS);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.CLOSE_BRACKET.hasEvent(event)) {
            input.setAction(MenuAction.NEXT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyTyped(KeyboardEvent event, UserInput userInput) {
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyReleased(KeyboardEvent event, UserInput input) {
        return UIEventResponses.pass();
    }
}
