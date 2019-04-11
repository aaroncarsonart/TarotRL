package com.aaroncarsonart.tarotrl.deck;


import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.util.Logger;

public class TarotRL {
    private static final Logger LOG = new Logger(TarotRL.class);

    private TarotDeck TarotDeck;

    public void init() {
        JsonDefinitionLoader loader = new JsonDefinitionLoader();
        TarotDeck tarotDeck = loader.loadTarotDeck();
        tarotDeck.initOrdering();
        tarotDeck.riffleShuffle();

        for (TarotCard tarotCard : tarotDeck.getCards()) {
            LOG.info(tarotCard);
        }
    }
}
