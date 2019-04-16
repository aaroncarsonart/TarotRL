package com.aaroncarsonart.tarotrl.input;

import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;

/**
 * Store key info, and if they come from a keycode vs the key string.
 */
public enum Key {
    ENTER(KeyCode.ENTER),
    ESCAPE(KeyCode.ESCAPE),
    SHIFT(KeyCode.SHIFT),
    UP(KeyCode.UP),
    DOWN(KeyCode.DOWN),
    LEFT(KeyCode.LEFT),
    RIGHT(KeyCode.RIGHT),
    A("a"),
    B("b"),
    C("c"),
    SPACE(" "),
    ;

    String key;
    KeyCode keyCode;
    boolean pressed;
    boolean consumed;

    Key(String key) {
        this.key = key;
    }

    Key(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Press this command.
     */
    public void press() {
        if (!pressed) {
            pressed = true;
            consumed = false;
            System.out.printf("%s pressed\n", this.name());
        }
    }

    /**
     * Release this command.
     */
    public void release() {
        if (pressed) {
            pressed = false;
            consumed = true;
            System.out.printf("%s released\n", this.name());
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }

    boolean isKeyCode() {
        return keyCode != null;
    }

    boolean hasEvent(KeyboardEvent event) {
        if (isKeyCode()) {
            return keyCode == event.getCode();
        } else {
            return key.equals(event.getKey());
        }
    }
}

