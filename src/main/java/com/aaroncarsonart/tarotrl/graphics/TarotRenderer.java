package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.deck.TarotCardType;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;

/**
 * Encapsulate all logic for the drawing of tarot cards to the TileGrid.
 */
public class TarotRenderer extends GameTileRenderer {
    private static final Logger LOG = new Logger(TarotRenderer.class).withLogLevel(LogLevel.INFO);

    private static final char TL = '╔';
    private static final char TR = '╗';
    private static final char ML = 'L';
    private static final char MR = 'R';
    private static final char BL = '╚';
    private static final char BR = '╝';

    private char[][] cardTemplate;

    public TarotRenderer() {
        cardTemplate = new char[][]{
                {TL, TR},
                {ML, MR},
                {BL, BR}};
    }

    protected void renderTarotCardTest(TileGrid tileGrid, ViewPort viewPort) {
        int sx = viewPort.x + 2;
        int sy = viewPort.y + 2;

        renderTarotCard(tileGrid, "2S", Positions.create(sx, sy));
        renderTarotCard(tileGrid, "3C", Positions.create(sx + 6, sy));
        renderTarotCard(tileGrid, "4W", Positions.create(sx + 6, sy + 8));
        renderTarotCard(tileGrid, "1P", Positions.create(sx, sy + 8));
        renderTarotCard(tileGrid, "15", Positions.create(sx + 3, sy + 4));

        renderTarotDeck(tileGrid, Positions.create(sx + 9, sy - 1));

        renderBackOfTarotCard(tileGrid, Positions.create(sx + 10, sy + 4));
        renderBackOfTarotCard(tileGrid, Positions.create(sx + 11, sy + 8));

        // Print all of the MINOR ARCANA cards.
        sx = sx + 16;
        int yOffset = 0;
        for (TarotCardType minorArcana : TarotCardType.getMinorArcana()) {
            for (int i = 1; i <= 14; i++) {
                char rank = getRankSymbol(i);
                char suit = minorArcana.suit;
                String key = "" + rank + suit;

                int xOffset = (i - 1) * 3;
                Position pos = Positions.create(sx + xOffset, sy + yOffset);

                renderTarotCard(tileGrid, key, pos);
            }
            yOffset += 4;
        }

        // Print all of the MAJOR ARCANA cards.
        for (int i = 0; i <= 21; i++) {
            int rank = i;
            String key = String.format("%2d", rank);
            if (rank < 10) {
                key = "║" + key.substring(1);
            }


            int xOffset = 4 + (i % 11) * 3;
            int yOffset2 = yOffset + (i / 11) * 4;
            Position pos = Positions.create(sx + xOffset, sy + yOffset2);

            renderTarotCard(tileGrid, key, pos);
        }
    }

    protected void renderTarotCardTest_closelySpaced(TileGrid tileGrid, ViewPort viewPort) {
        int sx = viewPort.x + 2;
        int sy = viewPort.y + 2;

        renderTarotCard(tileGrid, "2S", Positions.create(sx, sy));
        renderTarotCard(tileGrid, "3C", Positions.create(sx + 4, sy));
        renderTarotCard(tileGrid, "4W", Positions.create(sx + 4, sy + 6));
        renderTarotCard(tileGrid, "1P", Positions.create(sx, sy + 6));
        renderTarotCard(tileGrid, "15", Positions.create(sx + 2, sy + 3));
    }

    protected void renderTarotDeck(TileGrid tileGrid, Position pos) {
        renderBackOfTarotCard(tileGrid, pos);

        Tile edgeTile = Tiles.newBuilder()
                .withForegroundColor(TarotCardType.EDGE.fgColor)
                .withBackgroundColor(TarotCardType.EDGE.darkColor)
                .withCharacter(TarotCardType.EDGE.symbol)
                .buildCharacterTile();

        Position leftEdgePosition = Positions.create(pos.getX(), pos.getY() + 3);
        Position rightEdgePosition = Positions.create(pos.getX() + 1, pos.getY() + 3);
        tileGrid.setTileAt(leftEdgePosition, edgeTile);
        tileGrid.setTileAt(rightEdgePosition, edgeTile);
    }

    protected void renderBackOfTarotCard(TileGrid tileGrid, Position pos) {
        // ░▒▓
        renderTarotCard(tileGrid, "░║", pos, GameColors.CYAN_5, GameColors.CYAN_2);
        renderTarotCardAsOverlay(tileGrid, "║║", pos, GameColors.CYAN_1, TileColors.transparent());
    }

    protected void renderTarotCard(TileGrid tileGrid, String key, Position pos) {
        renderTarotCard(tileGrid, key, pos, null, null, false);
    }

    protected void renderTarotCard(TileGrid tileGrid, String key, Position pos, TileColor fg, TileColor bg) {
        renderTarotCard(tileGrid, key, pos, fg, bg, false);
    }

    protected void renderTarotCardAsOverlay(TileGrid tileGrid, String key, Position pos, TileColor fg, TileColor bg) {
        renderTarotCard(tileGrid, key, pos, fg, bg, true);
    }

    protected void renderTarotCard(TileGrid tileGrid, String key, Position pos, TileColor fg, TileColor bg, boolean asOverlay) {
        char rankChar = key.charAt(0);
        char suitChar = key.charAt(1);
        TarotCardType suit = TarotCardType.fromCharacter(suitChar);

        if (suit.symbol != null) {
            suitChar = suit.symbol;
        }

        if (fg == null) {
            fg = suit.symbolColor;
        }
        if (bg == null) {
            bg = suit.fgColor;
        }

        if (asOverlay) {
            bg = TileColors.transparent();
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 2; x++) {
                Tile tile;
                if (suit == TarotCardType.BACK && rankChar != suit.symbol) {
                    Tile backTile = Tiles.newBuilder()
                            .withForegroundColor(fg)
                            .withBackgroundColor(bg)
                            .withCharacter(rankChar)
                            .buildCharacterTile();
                    tile = backTile;
                } else {
                    char templateChar = cardTemplate[y][x];
                    switch (templateChar) {
                        case ML:
                            Tile rankTile = Tiles.newBuilder()
                                    .withForegroundColor(fg)
                                    .withBackgroundColor(bg)
                                    .withCharacter(rankChar)
                                    .buildCharacterTile();
                            tile = rankTile;
                            break;
                        case MR:
                            Tile suitTile = Tiles.newBuilder()
                                    .withForegroundColor(fg)
                                    .withBackgroundColor(bg)
                                    .withCharacter(suitChar)
                                    .buildCharacterTile();
                            tile = suitTile;
                            break;
                        default:
                            Tile borderTile = Tiles.newBuilder()
                                    .withForegroundColor(fg)
                                    .withBackgroundColor(bg)
                                    .withCharacter(templateChar)
                                    .buildCharacterTile();
                            tile = borderTile;
                            break;
                    }
                }
                Position next = Positions.create(pos.getX() + x, pos.getY() + y);

                if (asOverlay) {
                    Layer overlay = tileGrid.getLayers().get(0);
                    overlay.setTileAt(next, tile);
                } else {
                    tileGrid.setTileAt(next, tile);
                }
            }
        }
    }

    protected char getRankSymbol(int rank) {
        switch (rank) {
            case 10: return 'X';
            case 11: return 'p';
            case 12: return 'k';
            case 13: return 'Q';
            case 14: return 'K';
            default: return Character.forDigit(rank, 10);
        }
    }
}
