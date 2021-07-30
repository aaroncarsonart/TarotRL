package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.input.CardSelectionAction;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.GameMap3D;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Encapsulates logic for controlling the CARD_SELECTION GameMode.
 */
public class CardSelectionController implements GameController {
    private static final Logger LOG = new Logger(CardSelectionController.class);

    private Callback cancelCallback;
    private BiConsumer<GameState, TarotCardItem> selectCallback;

    @Override
    public boolean update(GameState state, UserInput input) {
        Enum action = input.getAction();
        LOG.info("action:" + action.getClass().getSimpleName() + "." + action.name());
        if (action instanceof CardSelectionAction) {
            CardSelectionAction cardAction = (CardSelectionAction) action;
            int maxCardIndex = state.getPlayersTarotDeck().size() - 1;
            CardSelectionData cardSelectionData = state.getCardSelectionData();
            int selectedCardIndex = cardSelectionData.getSelectedCardIndex();
            switch (cardAction) {
                case PREVIOUS:
                    selectedCardIndex--;
                    if (selectedCardIndex < 0) {
                        selectedCardIndex = maxCardIndex;
                    }
                    break;
                case NEXT:
                    selectedCardIndex++;
                    if (selectedCardIndex > maxCardIndex) {
                        selectedCardIndex = 0;
                    }
                    break;
                case SELECT:
                    List<TarotCardItem> cards = state.getPlayersTarotDeck();
                    TarotCardItem selectedCard = cards.get(selectedCardIndex);
                    selectCallback.accept(state, selectedCard);
                    break;
                case CANCEL:
                    cancelCallback.execute();
                    break;
            }
            cardSelectionData.setSelectedCardIndex(selectedCardIndex);
            LOG.info("selectedCardIndex: " + selectedCardIndex);
            return true;
        } else {
            LOG.info("action not handled.");
        }
        return false;
    }

    /**
     * Warp the player to the GameMap associated with the selected TarotCard.
     * @param state The GameState.
     * @param selectedCard The selected TarotCard to look up the GameMap with.
     */
    public void warpToGameMapUsingTarotCard(GameState state, TarotCardItem selectedCard) {
        LOG.info("warpToGameMapUsingTarotCard()");
        GameMap3D firstGameMap = state.getMapFromTarotCard(selectedCard.getTarotCard());
        TileDefinitionSet tileDefinitionSet = firstGameMap.getTileDefinitionSet();
        TileType.setTileTypeMetadata(tileDefinitionSet);
        state.setActiveGameMap(firstGameMap);
        state.setGameMode(GameMode.MAP_NAVIGATION);
    }

    /**
     * Used at the beginning of the game. Selects the player's starting tarot card,
     * adds this given card to the player's deck, and warps the player to the given
     * card's associated GameMap.
     * @param state The GameState.
     * @param selectedCard The selected TarotCard to add to the player's deck, and
     *                     to look up the associated GameMap to warp the player to.
     */
    public void selectStartingTarotCard(GameState state, TarotCardItem selectedCard) {
        LOG.info("selectStartingTarotCard()");
        warpToGameMapUsingTarotCard(state, selectedCard);
        state.getPlayersTarotDeck().clear();
        state.addTarotCardToDeck(selectedCard);
        LOG.info("deck size: " + state.getPlayersTarotDeck().size());
    }

    public void setCancelCallback(Callback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }

    public void setSelectCallback(BiConsumer<GameState, TarotCardItem> selectCallback) {
        this.selectCallback = selectCallback;
    }
}
