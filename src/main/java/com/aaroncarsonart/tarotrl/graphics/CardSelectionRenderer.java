package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.CardSelectionData;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.List;

/**
 * Encapsulates logic for rendering the CARD_SELECTION GameMode.
 */
public class CardSelectionRenderer extends TarotRenderer {
    private static final Logger LOG = new Logger(CardSelectionRenderer.class).withLogLevel(LogLevel.INFO);

    private ViewPort mapViewPort;

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        if (mapViewPort == null) {
            mapViewPort = MapTileRenderer.createMapViewPort(tileGrid);
        }
        tileGrid.clear();

        int width = tileGrid.getWidth();
        int height = tileGrid.getHeight();

        int mx = width / 2;
        int my = height / 3;

        // write message prompt
        CardSelectionData cardSelectionData = gameState.getCardSelectionData();
        List<String> messagePrompt = cardSelectionData.getMessagePrompt();
        int longestMessageLength = messagePrompt.stream()
                .map(String::length)
                .max(Integer::compare)
                .orElseThrow(() -> new IllegalStateException("Expected an int from message prompt length."));

        int bx = mx - longestMessageLength / 2;
        int by = 0;
        int bWidth = longestMessageLength + 2;
        writeTextInBox(tileGrid, messagePrompt, TextAlignment.CENTER, bx, by, bWidth);

        // render selected card
        int selectedCardIndex = cardSelectionData.getSelectedCardIndex();
        LOG.info("selectedCardIndex: " + selectedCardIndex);
        List<TarotCardItem> cards = gameState.getPlayersTarotDeck();
        TarotCardItem cardItem = cards.get(selectedCardIndex);
        TarotCard card = cardItem.getTarotCard();
        String key = card.getShorthandName();

        Position cardPosition = Positions.create(mx, my);
        renderTarotCard(tileGrid, key, cardPosition);

        // render list of card names
        int deckSize = cards.size();
        int listTop = my * 2 + 1;
        int listBottom = my * 3 - 1;
        int listMiddle = my * 2 + my / 2;

        listTop = Math.max(listTop, listMiddle - deckSize / 2);
        listBottom = Math.min(listBottom, listTop + deckSize);

        int listLength = listBottom - listTop;
        int middleOffset = listMiddle - listTop;

        LOG.info("deckSize: " + deckSize);
//        for (TarotCardItem item : cards) {
//            TarotCard currentCard = item.getTarotCard();
//            int order = currentCard.getOrder();
//            String name = currentCard.getDisplayName();
//            LOG.info("[" + order + "]: " + name);
//        }

        int maxLength = gameState.getAllTarotCards().getMaxDisplayNameLength();

        // draw box around card names
        int vx = mx - maxLength / 2 - 1;
        int vy = listTop - 1;
        int vWidth = maxLength + 2;
        int vHeight = listLength + 2;
        ViewPort cardNameViewPort = new ViewPort(vx, vy, vWidth, vHeight);
        drawSimpleBorder(tileGrid, cardNameViewPort, false);

        // write list of card names
        for (int i = 0; i < listLength; i++) {
            int cardIndex = selectedCardIndex - middleOffset + i;

            // adjust if cardIndex is too small
            if (cardIndex < 0) {
                cardIndex += deckSize;
            }
            // adjust if cardIndex is too large
            if (cardIndex >= deckSize) {
                cardIndex -= deckSize;
            }
            LOG.info("cardIndex: " + cardIndex);
            TarotCardItem currentCardItem = cards.get(cardIndex);
            TarotCard currentCard = currentCardItem.getTarotCard();
            String cardName = currentCard.getDisplayName();

            int cLength = cardName.length();
            int cx = mx - cLength / 2;
            int cy = listTop + i;

            TileColor fg, bg;
            if (cardIndex == selectedCardIndex) {
                fg = GameColors.BLACK;
                bg = currentCard.getTarotCardType().symbolColor;
            } else {
                fg = currentCard.getTarotCardType().symbolColor;
                bg = GameColors.BLACK;
            }

            writeText(tileGrid, cardName, cx, cy, fg, bg);
        }
    }
}
