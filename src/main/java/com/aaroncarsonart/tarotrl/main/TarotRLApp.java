package com.aaroncarsonart.tarotrl.main;

import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.Game;
import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameModeComponents;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.CardSelectionController;
import com.aaroncarsonart.tarotrl.game.controller.CardSelectionData;
import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.game.controller.InventoryGameController;
import com.aaroncarsonart.tarotrl.game.controller.MapController;
import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.CardSelectionRenderer;
import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.graphics.InventoryTileRenderer;
import com.aaroncarsonart.tarotrl.graphics.MapTileRenderer;
import com.aaroncarsonart.tarotrl.graphics.SnapshotHistory;
import com.aaroncarsonart.tarotrl.input.CardSelectionInputHandler;
import com.aaroncarsonart.tarotrl.input.InventoryInputHandler;
import com.aaroncarsonart.tarotrl.input.MapInputHandler;
import com.aaroncarsonart.tarotrl.inventory.Item;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.resource.TilesetResource;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * The TarotRL App!
 */
public class TarotRLApp {
    private static final String TAROT_RL_GAME_TITLE = "TarotRL";

    private static GameModeComponents createCardSelectionComponents() {
        CardSelectionInputHandler cardSelectionInputHandler = new CardSelectionInputHandler();
        CardSelectionController cardSelectionController = new CardSelectionController();
        CardSelectionRenderer cardSelectionRenderer = new CardSelectionRenderer();

        GameModeComponents cardSelectionComponents = new GameModeComponents(
                cardSelectionInputHandler,
                cardSelectionController,
                cardSelectionRenderer
        );
        return cardSelectionComponents;
    }

    private static GameModeComponents createMapModeComponents() {
        MapInputHandler mapInputHandler = new MapInputHandler();
        MapController mapController = new MapController();
        MapTileRenderer mapTileRenderer = new MapTileRenderer();

        GameModeComponents mapComponents = new GameModeComponents(
                mapInputHandler,
                mapController,
                mapTileRenderer);
        return mapComponents;
    }

    private static GameModeComponents createInventoryModeComponents() {
        InventoryInputHandler inventoryInputHandler = new InventoryInputHandler();
        InventoryGameController inventoryGameController = new InventoryGameController();
        InventoryTileRenderer inventoryTileRenderer = new InventoryTileRenderer();

        GameModeComponents inventoryComponents = new GameModeComponents(
                inventoryInputHandler,
                inventoryGameController,
                inventoryTileRenderer);
        return inventoryComponents;
    }

    private static GameModeComponents createHelpModeComponents() {
        return null;
    }

    private static Size getWindowDimensions(TilesetResource tileSet) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth() - 50;
        int windowHeight = ((int) screenHeight) / tileSet.getHeight() - 10;
        return Sizes.create(windowWidth, windowHeight);
    }

    /**
     * Create all needed for the TarotRL game, up to right before calling {@link Game#start()}.
     * @return The fully configured Game instance that is ready to be played.
     */
    private static Game createTarotRLGame() {
        GameStateGenerator gameStateGenerator = new GameStateGenerator();
        GameState gameState = gameStateGenerator.generateTarotRLGameState();

        TilesetResource tilesetResource = CP437TilesetResources.mdCurses16x16();
        Size windowDimensions = getWindowDimensions(tilesetResource);

        GraphicsContext graphicsContext = new GraphicsContext();
        graphicsContext.setWindowTitle(TAROT_RL_GAME_TITLE);
        graphicsContext.setTilesetResource(tilesetResource);
        graphicsContext.setWindowDimensions(windowDimensions);

        GameModeComponents mapComponents = createMapModeComponents();
        GameModeComponents inventoryComponents = createInventoryModeComponents();
        SnapshotHistory snapshotHistory = new SnapshotHistory();
        graphicsContext.addObserver(snapshotHistory);
        GameModeComponents cardSelectionComponents = createCardSelectionComponents();

        Game game = new Game();
        game.setGameState(gameState);
        game.registerGameMode(GameMode.MAP_NAVIGATION, mapComponents);
        game.registerGameMode(GameMode.INVENTORY, inventoryComponents);
        game.registerGameMode(GameMode.SNAPSHOT_HISTORY, snapshotHistory.asGameModeComponents());
        game.registerGameMode(GameMode.CARD_SELECTION, cardSelectionComponents);
        game.setGraphicsContext(graphicsContext);

        gameState.setGameMode(GameMode.CARD_SELECTION);
        GameController controller = GameMode.CARD_SELECTION.getGameController();
        CardSelectionController cardSelectionController = (CardSelectionController) controller;
        cardSelectionController.setCancelCallback(() -> {});
        cardSelectionController.setSelectCallback(cardSelectionController::selectStartingTarotCard);

        CardSelectionData cardSelectionData = gameState.getCardSelectionData();
        List<String> messagePrompt = List.of(
                CardSelectionData.STARTING_PROMPT_MESSAGE_1,
                CardSelectionData.STARTING_PROMPT_MESSAGE_2);
        cardSelectionData.setMessagePrompt(messagePrompt);
        cardSelectionData.setSelectedCardIndex(0);

        initDevPlayerStatus(gameState);

        return game;
    }

    private static void initDevPlayerStatus(GameState state) {
        List<String> startingTarotCardNames = List.of(
                "0. The Fool",
                "1 of Pentacles",
                "1 of Swords",
                "1 of Wands",
                "1 of Cups");

        // clear deck to empty state
        state.getPlayersTarotDeck().clear();

        // fetch starting tarot cards
        for (String tarotCardName : startingTarotCardNames) {
            // fetch tarot card by name
            TarotCard cardToFetch = state.getAllTarotCards().getCards().stream()
                    .filter(c -> c.getDisplayName().equals(tarotCardName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Card name not not found: " + tarotCardName));

            // load map and set as active map
            for (TarotCard cardForLoadingMap : state.getAllTarotCards().getCards()) {
                GameMap3D gameMap = state.getMapFromTarotCard(cardForLoadingMap);

                // check for a match against
                Predicate<MapEntity> findTarotCard = entity -> {
                    if (entity instanceof ItemEntity) {
                        ItemEntity itemEntity = (ItemEntity) entity;
                        Item item = itemEntity.getItem();
                        return item instanceof TarotCardItem;
                    }
                    return false;
                };

                ItemEntity tarotCardItemEntity = (ItemEntity) gameMap.getFirstMapEntityMatching(findTarotCard);
                if (tarotCardItemEntity != null) {
                    TarotCardItem mapTarotCardItem = (TarotCardItem) tarotCardItemEntity.getItem();
                    TarotCard mapTarotCard = mapTarotCardItem.getTarotCard();
                    String displayName = mapTarotCard.getDisplayName();
                    if (tarotCardName .equals(displayName)) {
                        state.setActiveGameMap(gameMap);

                        // auto collect the card item from the map
                        MapController mapController = (MapController) GameMode.MAP_NAVIGATION.getGameController();
                        mapController.doCollectTarotCard(state);
                        break;
                    }
                }
            }
        }

        state.getPlayersTarotDeck().sort(Comparator.naturalOrder());

        // load first card's game map
        int startingSelectedCardIndex = 0;
        TarotCardItem firstCardItem = state.getPlayersTarotDeck().get(startingSelectedCardIndex);
        GameController controller = GameMode.CARD_SELECTION.getGameController();
        CardSelectionController cardSelectionController = (CardSelectionController) controller;
        cardSelectionController.warpToGameMapUsingTarotCard(state, firstCardItem);
        CardSelectionData cardSelectionData = state.getCardSelectionData();
        cardSelectionData.setSelectedCardIndex(startingSelectedCardIndex);
    }


    /**
     * Run the game.
     * @param args Not used.
     */
    public static void main(String[] args) {
        Game tarotRL = createTarotRLGame();
        tarotRL.start();
    }
}
