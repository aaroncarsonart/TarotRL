package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GameWorldRenderer;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.graphics.ViewPort;
import com.aaroncarsonart.tarotrl.input.InputHandler;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Layers;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The main class for encapsulating all game logic when using the Zircon UI.
 */
public class Game {

    public static void runZirconGame() {
        GameStateGenerator gameStateGenerator = new GameStateGenerator();
        GameState gameState = gameStateGenerator.generateInitialGameState();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

        // Hey, neat!  Some tilesets have graphics build in.  For example: aesomatica16x16
        TilesetResource tileSet = CP437TilesetResources.mdCurses16x16();

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth();
        int windowHeight = ((int) screenHeight) / tileSet.getHeight();

        AppConfig appConfig = AppConfigs.newConfig()
                .withSize(Sizes.create(windowWidth, windowHeight))
                .withDefaultTileset(tileSet)
                .build();

        TileGrid tileGrid = SwingApplications.startTileGrid(appConfig);
        tileGrid.pushLayer(Layers.newBuilder().build());

        int xOffset = 20;
        int topOffSet = 1;
        int bottomOffSet = 10;
        Position mapOffset = Positions.create(xOffset, topOffSet);
        Size mapDimensions = Sizes.create(windowWidth - (xOffset * 2), windowHeight - (topOffSet + bottomOffSet));
        ViewPort mapViewPort = new ViewPort(mapOffset, mapDimensions);

        Layer mapLayer1 = new LayerBuilder()
                .withOffset(mapOffset)
                .withSize(mapDimensions)
                .build();

        tileGrid.pushLayer(mapLayer1);

        GameActionHandler actionHandler = new GameActionHandler();
        InputHandler inputHandler = new InputHandler();

        tileGrid.onKeyStroke(keyStroke -> {
            inputHandler.handleKeyStroke(keyStroke);
            gameState.setDevMode(inputHandler.getDevMove());
            gameState.setShiftDown(inputHandler.getShiftDown());
        });

        TileRenderer tileRenderer = new GameWorldRenderer();
        tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
        tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);

        while (true) {
            PlayerAction nextAction = inputHandler.consumeNextAction();
            if (nextAction != PlayerAction.UNKNOWN) {
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
                tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        runZirconGame();
    }
}
