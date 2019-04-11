package com.aaroncarsonart.tarotrl.deck;


import com.aaroncarsonart.tarotrl.util.Globals;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.util.Logger;

import java.io.IOException;
import java.net.URL;

public class TarotRL {
    private static final Logger LOG = new Logger(TarotRL.class);

    private TarotDeck TarotDeck;

    public void init() {
        TarotDeck tarotDeck = loadDeck();
        tarotDeck.initOrdering();
        tarotDeck.riffleShuffle();

        for (TarotCard tarotCard : tarotDeck.getCards()) {
            LOG.info(tarotCard);
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
            throw new TarotRLException(e);
        }
    }
}
