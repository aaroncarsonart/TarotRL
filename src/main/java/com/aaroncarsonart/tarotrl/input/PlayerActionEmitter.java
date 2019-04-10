package com.aaroncarsonart.tarotrl.input;

import java.util.ArrayList;
import java.util.List;

/**
 * Receives {@link PlayerAction PlayerActions}, and broadcasts them
 * to registered {@link PlayerActionListener PlayerActionListeners}.
 */
public class PlayerActionEmitter {

    private List<PlayerActionListener> listeners = new ArrayList<>();

    public void broadcastPlayerAction(PlayerAction event) {
        for(PlayerActionListener listener : listeners) {
            listener.onPlayerAction(event);
        }
    }

    public void addListener(PlayerActionListener listener) {
        listeners.add(listener);
    }
}
