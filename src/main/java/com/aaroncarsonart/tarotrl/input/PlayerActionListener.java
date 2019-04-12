package com.aaroncarsonart.tarotrl.input;

/**
 * Listener callback for {@link PlayerActionEmitter}.
 */
public interface PlayerActionListener {
    void onPlayerAction(PlayerAction action);

    // TODO: add possible new listener method, onMove or some such.
}
