package com.aaroncarsonart.tarotrl.deck;


import com.aaroncarsonart.tarotrl.Globals;

import java.io.IOException;
import java.net.URL;

public class TarotRL {

    private TarotDeck TarotDeck;

    public void init() {
        TarotDeck tarotDeck = loadDeck();
        tarotDeck.initOrdering();
        tarotDeck.riffleShuffle();

        for (TarotCard tarotCard : tarotDeck.getCards()) {
            System.out.println(tarotCard);
        }
    }

    /**
     * Load The deck deck from the config.
     * @return The deck deck.
     */
    private TarotDeck loadDeck() {
        URL url = TarotDeck.class.getResource("tarot_deck.json");
        try {
            TarotDeck tarotDeck = Globals.OBJECT_MAPPER.readValue(url, TarotDeck.class);
            return tarotDeck;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
