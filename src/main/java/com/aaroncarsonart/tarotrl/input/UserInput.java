package com.aaroncarsonart.tarotrl.input;

import java.util.HashSet;
import java.util.Set;

/**
 * Hold all variables and logic related to the user input.
 * @see InputHandler
 */
public class UserInput {
    /**
     * Modifiers are flags for modifying how inputs are to be handled by
     * a given GameController.  Modifiers can be enabled or disabled.
     * <p>
     * Key input for these states are usually typing a key to enable/disable a mode,
     * or holding down a key to enable this mode while the key is pressed.
     * <p>
     * Multiple modifiers may exist at once.
     * <p>
     * Enabling or disabling a modifier will result in an action.
     */
    private Set<Enum> modifiers = new HashSet<>();

    /**
     * The current game action to be executed.
     */
    private Enum action = PlayerAction.UNKNOWN;
    /**
     * The game action that was previously executed.
     */
    private Enum previousAction = PlayerAction.UNKNOWN;

    private Object lock = new Object();

    public void setAction(Enum action) {
        this.action = action;
    }

    public Enum getAction() {
        return action;
    }

    public void retireCurrentAction() {
        previousAction = action;
        action = PlayerAction.UNKNOWN;
    }

    public Object getLock() {
        return lock;
    }

    public void doUpdateController() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public boolean hasModifer(Enum modifier) {
        return modifiers.contains(modifier);
    }

    public void setModifier(Enum modifier, boolean enable) {
        if (enable) {
            modifiers.add(modifier);
        } else {
            modifiers.remove(modifier);
        }
    }

    public void enableModifier(Enum modifier) {
        modifiers.add(modifier);
    }

    public void disableModifier(Enum modifier) {
        modifiers.remove(modifier);
    }
}


