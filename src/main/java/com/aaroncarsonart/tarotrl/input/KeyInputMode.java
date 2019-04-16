package com.aaroncarsonart.tarotrl.input;

/**
 * How to handle inputs for a given key.
 */
public enum KeyInputMode {
    /**
     * Trigger a response only when the key is pressed.
     */
    ON_KEY_PRESS,
    /**
     * Trigger a response only when the key is released.
     */
    ON_KEY_RELEASE,

    /**
     * .
     */
    WHILE_PRESSED,

}
