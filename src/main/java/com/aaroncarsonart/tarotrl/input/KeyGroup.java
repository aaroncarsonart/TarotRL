package com.aaroncarsonart.tarotrl.input;

import org.hexworks.zircon.api.uievent.KeyboardEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * A KeyGroup is a logical grouping of {@link Key Keys} where many can be pressed
 * at a time, but only one is currently active.  This allows for smooth handling
 * of movement keys, for example.
 */
public class KeyGroup<T extends Enum<T>> {

    private Stack<Key> pressedKeys;
    private Map<Key, T> keyActionMapping;

    public KeyGroup() {
        this.pressedKeys = new Stack<>();
        this.keyActionMapping = new EnumMap<>(Key.class);
    }

    public void addMapping(Key key, T action) {
        keyActionMapping.put(key, action);
    }

    public boolean hasPressedKeys() {
        return !pressedKeys.isEmpty();
    }

    public Key getKey() {
        return pressedKeys.peek();
    }

    public T getAction() {
        Key key = getKey();
        return keyActionMapping.get(key);
    }


    public boolean keyPressed(KeyboardEvent event, Consumer<T> actionCallback) {
        keyPressed(event);
        return applyGroupedKeyAction(actionCallback);
    }

    public boolean keyPressed(KeyboardEvent event) {
        for (Key key : keyActionMapping.keySet()) {
            if (keyPressed(event, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean keyPressed(KeyboardEvent event, Key key) {
        if (key.hasEvent(event) && !key.isPressed() && !key.isConsumed()) {
            key.press();
            pressedKeys.push(key);
            return true;
        }
        return false;
    }

    public boolean keyReleased(KeyboardEvent event, Consumer<T> actionCallback) {
        keyReleased(event);
        return applyGroupedKeyAction(actionCallback);
    }

    public boolean keyReleased(KeyboardEvent event) {
        for (Key key : keyActionMapping.keySet()) {
            if (keyReleased(event, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean keyReleased(KeyboardEvent event, Key key) {
        if (key.hasEvent(event)) {
            key.release();
            pressedKeys.remove(key);
            return true;
        }
        return false;
    }

    private boolean applyGroupedKeyAction(Consumer<T> actionCallback){
        if (!pressedKeys.isEmpty()) {
            Key move = pressedKeys.peek();
            T action = keyActionMapping.get(move);
            actionCallback.accept(action);
            return true;
        }
        return false;
    }

}
