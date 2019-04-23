package com.aaroncarsonart.tarotrl.deck;

import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;

/**
 * An "element" is one of the 5 elements that make up the
 * Pentagram: Air, Water, Fire, Earth, and Spirit.
 */
public enum Element {
    AIR(TarotCardType.SWORDS),
    WATER(TarotCardType.CUPS),
    FIRE(TarotCardType.WANDS),
    EARTH(TarotCardType.PENTACLES),
    SPIRIT(TarotCardType.MAJOR_ARCANA),
    ;

    private TarotCardType cardType;
    private TileDefinitionSet tileDefinitions;

    Element(TarotCardType cardType) {
        this.cardType = cardType;
    }

    static {
        JsonDefinitionLoader loader = new JsonDefinitionLoader();
        for (Element element : Element.values()) {
            element.name().toLowerCase();
            TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/imbroglio.json");
            element.tileDefinitions = tileDefinitionSet;


        }

    }

}
