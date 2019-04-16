package com.aaroncarsonart.tarotrl.input;

import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

public class InventoryInputHandler implements InputHandler {
    private static final Logger LOG = new Logger(InventoryInputHandler.class);

    private KeyGroup<GameAction> moveKeys;

    public InventoryInputHandler() {
        moveKeys = new KeyGroup<>();
        moveKeys.addMapping(Key.UP, GameAction.MOVE_UP);
        moveKeys.addMapping(Key.DOWN, GameAction.MOVE_DOWN);
        moveKeys.addMapping(Key.LEFT, GameAction.MOVE_LEFT);
        moveKeys.addMapping(Key.RIGHT, GameAction.MOVE_RIGHT);
    }

    private boolean keyPressed(KeyboardEvent event, Key key) {
        if (key.hasEvent(event) && !key.isPressed() && !key.isConsumed()) {
            key.press();
            return true;
        }
        return false;
    }

    private boolean keyReleased(KeyboardEvent event, Key key) {
        if (key.hasEvent(event) && !key.isPressed() && !key.isConsumed()) {
            key.press();
            return true;
        }
        return false;
    }

//    private boolean keyPressed(KeyboardEvent event, Key key, Enum action, Consumer<Enum> callback) {
//        if (key.hasEvent(event) && !key.isPressed() && !key.isConsumed()) {
//            key.press();
//            callback.accept(action);
//            return true;
//        }
//        return false;
//    }

    @Override
    public UIEventResponse handleKeyPressed(KeyboardEvent event, UserInput input) {
        LOG.info(event);
        if (moveKeys.keyPressed(event) && moveKeys.hasPressedKeys()) {
            GameAction move = moveKeys.getAction();
            input.setCurrentAction(move);
            input.doNotifyAll();
            return UIEventResponses.processed();
        }
        if (keyPressed(event, Key.ENTER)) {
            input.setCurrentAction(GameAction.OK);
            input.doNotifyAll();
            return UIEventResponses.processed();
        }
        if (keyPressed(event, Key.ESCAPE)) {
            input.setCurrentAction(GameAction.CANCEL);
            input.doNotifyAll();
            return UIEventResponses.processed();
        }
        input.setCurrentAction(GameAction.NONE);
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyTyped(KeyboardEvent event, UserInput userInput) {
        LOG.info(event);
        return UIEventResponses.processed();
    }

    @Override
    public UIEventResponse handleKeyReleased(KeyboardEvent event, UserInput input) {
        LOG.info(event);
        if (moveKeys.keyReleased(event) && moveKeys.hasPressedKeys()) {
            GameAction move = moveKeys.getAction();
            input.setCurrentAction(move);
            input.doNotifyAll();
            return UIEventResponses.processed();
        }
        if (keyReleased(event, Key.ENTER) || keyReleased(event, Key.ESCAPE)) {
            return UIEventResponses.processed();
        }
        input.setCurrentAction(GameAction.NONE);
        return UIEventResponses.pass();
    }


    private void notify(UserInput input) {
        Object lock = input.getLock();
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
