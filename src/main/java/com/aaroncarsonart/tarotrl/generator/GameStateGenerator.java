package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.deck.TarotDeck;
import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.GameMap3D;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class GameStateGenerator {
    private static final Logger LOG = new Logger(GameStateGenerator.class);

    private JsonDefinitionLoader loader;
    private GameWorldGenerator gameWorldGenerator;

    public GameStateGenerator() {
        loader = new JsonDefinitionLoader();
        gameWorldGenerator = new GameWorldGenerator();
    }

    public GameState generateImbroglioGameState() {
        LOG.info("Generating Imbroglio GameState ...");
        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/imbroglio.json");
        TileType.setTileTypeMetadata(tileDefinitionSet);

        GameState gameState = new GameState();

        GameMap3D world = gameWorldGenerator.generateImbroglioWorld();
        gameState.setActiveGameMap(world);
        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.EMPTY);
        gameState.gainTreasure(50);

        return gameState;
    }

    public GameState generateTarotRLGameState() {
        LOG.info("Generating TarotRL GameState ...");
//        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/forest_green.json");
//        setTileTypeMetadata(tileDefinitionSet);

        GameState gameState = new GameState();

        // load and shuffle tarot deck
        TarotDeck tarotDeck = loader.loadTarotDeck();
        gameState.setAllTarotCards(tarotDeck);

        // save copy of list of cards to preserve ordering before shuffling
        List<TarotCard> cards = tarotDeck.getSetOfCards();
        tarotDeck.randomShuffle();

        // generate GameMaps based on list of cards
        for (TarotCard card : cards) {
            GameMap3D gameMap = gameWorldGenerator.generateTarotRLGameMap(gameState, card);
            gameState.addMap(card, gameMap);

            // Extract the generated TarotCardItem for use in CARD_SELECTION mode
            Predicate<MapEntity> isTarotCard = e -> e instanceof ItemEntity && ((ItemEntity) e).getItem() instanceof TarotCardItem;
            ItemEntity cardEntity = (ItemEntity) gameMap.getFirstMapEntityMatching(isTarotCard);
            TarotCardItem cardItem = (TarotCardItem) cardEntity.getItem();
            gameState.getPlayersTarotDeck().add(cardItem);
        }

        // reset TarotDeck to original ordering
        tarotDeck.setCards(cards);

        // sort the TarotCardItem list for use in CARD_SELECTION mode
        Collections.sort(gameState.getPlayersTarotDeck(), Comparator.comparingInt(c -> c.getTarotCard().getOrder()));

        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.WALL);

        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        return gameState;
    }
}
