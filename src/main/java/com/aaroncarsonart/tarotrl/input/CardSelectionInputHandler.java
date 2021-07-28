package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

/**
 * Handle user input for selection of the player's starting Tarot Card.
 */
public class CardSelectionInputHandler implements InputHandler {
    private static final Logger LOG = new Logger(CardSelectionInputHandler.class);

    @Override
    public UIEventResponse handleKeyPressed(KeyboardEvent event, UserInput input) {
        LOG.info(event);
        if (Key.ENTER.hasEvent(event)) {
            input.setAction(CardSelectionAction.SELECT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.ESCAPE.hasEvent(event) || Key.BACK_SLASH.hasEvent(event)) {
            input.setAction(CardSelectionAction.CANCEL);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.UP.hasEvent(event)) {
            input.setAction(CardSelectionAction.PREVIOUS);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.DOWN.hasEvent(event)) {
            input.setAction(CardSelectionAction.NEXT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.LEFT.hasEvent(event)) {
            input.setAction(CardSelectionAction.PREVIOUS);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        if (Key.RIGHT.hasEvent(event)) {
            input.setAction(CardSelectionAction.NEXT);
            input.doUpdateController();
            return UIEventResponses.processed();
        }
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyReleased(KeyboardEvent keyboardEvent, UserInput userInput) {
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyTyped(KeyboardEvent keyboardEvent, UserInput userInput) {
        return UIEventResponses.pass();
    }
}
