package com.aaroncarsonart.tarotrl.deck;

import com.aaroncarsonart.tarotrl.game.zircon.GameColors;
import org.hexworks.zircon.api.color.TileColor;

import java.util.EnumSet;
import java.util.Set;

/**
 * Associate symbols and colors with the suit and other info.
 */
public enum CardType {
    SWORDS      ('S',  '♠',  GameColors.BLUE_1,    GameColors.BLUE_2,    GameColors.BLUE_3,    GameColors.BLUE_4),
    CUPS        ('C',  '♥',  GameColors.GREEN_1,   GameColors.GREEN_2,   GameColors.GREEN_3,   GameColors.GREEN_4),
    WANDS       ('W',  '♣',  GameColors.RED_1,     GameColors.RED_2,     GameColors.RED_3   ,  GameColors.RED_4),
    PENTACLES   ('P',  '♦',  GameColors.YELLOW_1,  GameColors.YELLOW_2,  GameColors.YELLOW_3,  GameColors.YELLOW_4),
    MAJOR_ARCANA(null, null, GameColors.MAGENTA_1, GameColors.MAGENTA_2, GameColors.MAGENTA_3, GameColors.MAGENTA_4),

    //NOTE: These are for rendering the deck, not individual cards.
    BACK        ('║',  '║',  GameColors.CYAN_1,    GameColors.CYAN_2,    GameColors.CYAN_3,    GameColors.CYAN_4),
    EDGE        ('═',  '═',  GameColors.CYAN_1,    GameColors.CYAN_2,    GameColors.CYAN_3,    GameColors.CYAN_4);

    public final Character suit;
    public final Character symbol;
    public final TileColor symbolColor;
    public final TileColor fgColor;
    public final TileColor bgColor;
    public final TileColor darkColor;

    CardType(Character c, Character s, TileColor sc, TileColor fg, TileColor bg, TileColor dc) {
        this.suit = c;
        this.symbol = s;
        this.symbolColor = sc;
        this.fgColor = fg;
        this.bgColor = bg;
        this.darkColor = dc;
    }

    public static CardType fromCharacter(char letter) {
        if (SWORDS.suit.equals(letter)) return SWORDS;
        if (CUPS.suit.equals(letter)) return CUPS;
        if (WANDS.suit.equals(letter)) return WANDS;
        if (PENTACLES.suit.equals(letter)) return PENTACLES;
        if (BACK.suit.equals(letter)) return BACK;
        if (EDGE.suit.equals(letter)) return EDGE;
        return MAJOR_ARCANA;
    }

    public static Set<CardType> getMinorArcana() {
        return EnumSet.of(CardType.SWORDS, CardType.CUPS, CardType.WANDS, CardType.PENTACLES);
    }
}
