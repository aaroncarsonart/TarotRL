package com.aaroncarsonart.tarotrl.main;

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
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.resource.TilesetResource;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

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

        return game;
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
