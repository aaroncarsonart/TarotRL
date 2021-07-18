package com.aaroncarsonart.tarotrl.inventory;

import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.deck.TarotCardType;

/**
 * Represents a physical instance of a TarotCard encountered within a map in the game.
 * Contains a map of associations with AtonementCrystals, which are gained and removed
 * via the DAMAGE taken and received through interactions within the game.
 */
public class TarotCardItem extends Item {

    private TarotCard tarotCard;

    public TarotCardItem(TarotCard card) {
        super(generateCardName(card), generateCardDescription(card));
        this.tarotCard = card;
    }

    /**
     * Generate the card name used for the item representation of this Tarot Card.
     * @param card The TarotCard to generate the card name for.
     * @return The card name for the item representation of this Tarot Card.
     */
    private static String generateCardName(TarotCard card) {
        String majorArcana = "Major Arcana";
        String type;
        if (majorArcana.equals(card.getType())) {
            type = " MAJOR ARCANA";
        } else {
            type = "";
        }
        String name = "the \"" + card.getDisplayName() + "\"" + type + " tarot card";
        return name;
    }

    /**
     * Generate the card description used for the item representation of this Tarot Card.
     * @param card The TarotCard to generate the card description for.
     * @return The card description for the item representation of this Tarot Card.
     */
    private static String generateCardDescription(TarotCard card) {
        TarotCardType cardType = card.getTarotCardType();
        String description = "the face of the card is shining with a brilliant " +
                cardType.colorName + "ish sheen.";
        return description;
    }

    public TarotCard getTarotCard() {
        return tarotCard;
    }

    public void setTarotCard(TarotCard tarotCard) {
        this.tarotCard = tarotCard;
    }
}
