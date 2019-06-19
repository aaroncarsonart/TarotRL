package com.aaroncarsonart.tarotrl.main;

import com.aaroncarsonart.tarotrl.game.Game;
import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameModeComponents;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.MapController;
import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.graphics.ImbroglioTileRenderer;
import com.aaroncarsonart.tarotrl.input.MapInputHandler;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.resource.TilesetResource;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ImbroglioApp {
    private static final String IMBROGLIO_GAME_TITLE = "Imbroglio";

    private static GameModeComponents createMapModeComponents() {
        MapInputHandler inputHandler = new MapInputHandler();
        MapController gameController = new MapController();
        ImbroglioTileRenderer tileRenderer = new ImbroglioTileRenderer();

        GameModeComponents components = new GameModeComponents(
                inputHandler,
                gameController,
                tileRenderer);
        return components;
    }

    private static Size getWindowDimensions(TilesetResource tileSet) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth() - 5;
        int windowHeight = ((int) screenHeight) / tileSet.getHeight() - 5;
        return Sizes.create(windowWidth, windowHeight);
    }

    private static Game createImbroglioGame() {
        GameStateGenerator gameStateGenerator = new GameStateGenerator();
        GameState gameState = gameStateGenerator.generateImbroglioGameState();
        gameState.setGameMode(GameMode.MAP_NAVIGATION);

        TilesetResource tilesetResource = CP437TilesetResources.mdCurses16x16();
        Size windowDimensions = getWindowDimensions(tilesetResource);

        GraphicsContext graphicsContext = new GraphicsContext();
        graphicsContext.setWindowTitle(IMBROGLIO_GAME_TITLE);
        graphicsContext.setTilesetResource(tilesetResource);
        graphicsContext.setWindowDimensions(windowDimensions);

        GameModeComponents mapComponents =  createMapModeComponents();

        Game game = new Game();
        game.setGameState(gameState);
        game.registerGameMode(GameMode.MAP_NAVIGATION, mapComponents);
        game.setGraphicsContext(graphicsContext);

        return game;
    }

    public static void main(String[] args) {
        Game imbroglio = createImbroglioGame();
        imbroglio.start();
    }
}
