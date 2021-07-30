package com.aaroncarsonart.tarotrl.game.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates data used for the CARD_SELECTION GameMode.
 */
public class CardSelectionData {
    public static final String STARTING_PROMPT_MESSAGE_1 = "Select your starting Tarot Card";
    public static final String STARTING_PROMPT_MESSAGE_2 = "using the arrow keys.";
    public static final String WARP_PROMPT_MESSAGE_1 = "Select a Tarot Card to warp";
    public static final String WARP_PROMPT_MESSAGE_2 = "to its associated level.";

    private int selectedCardIndex = 0;
    private List<String> messagePrompt = new ArrayList<>();

    public int getSelectedCardIndex() {
        return selectedCardIndex;
    }

    public void setSelectedCardIndex(int selectedCardIndex) {
        this.selectedCardIndex = selectedCardIndex;
    }

    public List<String> getMessagePrompt() {
        return messagePrompt;
    }

    public void setMessagePrompt(List<String> messagePrompt) {
        this.messagePrompt = messagePrompt;
    }
}
