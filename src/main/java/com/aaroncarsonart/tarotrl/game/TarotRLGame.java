package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GameWorldRenderer;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.graphics.ViewPort;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.input.ZirconInputEventHandler;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Layers;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
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
public class TarotRLGame {

    private TileRenderer tileRenderer;
    private GameActionHandler actionHandler;
    private ZirconInputEventHandler inputHandler;
    private TileGrid tileGrid;
    private ViewPort mapViewPort;
    private GameState gameState;

    private void init() {
        GameStateGenerator gameStateGenerator = new GameStateGenerator();
        gameState = gameStateGenerator.generateTarotRLGameState();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

        TilesetResource tileSet = CP437TilesetResources.mdCurses16x16();

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth() - 40;
        int windowHeight = ((int) screenHeight) / tileSet.getHeight() - 10;

        int xOffset = 20;
        int topOffSet = 1;
        int bottomOffSet = 10;
        Position vOffset = Positions.create(xOffset, topOffSet);

        int vWidth = windowWidth - (xOffset) - 2;
        int vHeight = windowHeight - (topOffSet + bottomOffSet);

        Size vDimensions = Sizes.create(vWidth, vHeight);
        mapViewPort = new ViewPort(vOffset, vDimensions);

        actionHandler = new GameActionHandler();
        tileRenderer = new GameWorldRenderer();

        // Begin Displaying TileGrid
        tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withTitle("TarotRL")
                .withSize(Sizes.create(windowWidth, windowHeight))
                .withDefaultTileset(tileSet)
                .build());

        tileGrid.pushLayer(Layers.newBuilder().build());
        Layer layer1 = new LayerBuilder()
                .withOffset(vOffset)
                .withSize(vDimensions)
                .build();

        tileGrid.pushLayer(layer1);
        inputHandler = new ZirconInputEventHandler();
        inputHandler.listenForInput(tileGrid, gameState);
    }

    private void update() {
        tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);

        while (!gameState.isGameOver()) {
            PlayerAction nextAction = inputHandler.getNextAction();
            if (nextAction != PlayerAction.UNKNOWN) {
                inputHandler.clearNextAction();
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);
            }
        }
        // TODO: show a game over screen, based on the GameState.
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        TarotRLGame game = new TarotRLGame();
        game.init();
        game.update();
    }
}