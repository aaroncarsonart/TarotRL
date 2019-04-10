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

public class ImbroglioGame {

    private TileRenderer tileRenderer;
    private GameActionHandler actionHandler;
    private ZirconInputEventHandler inputHandler;
    private TileGrid tileGrid;
    private ViewPort mapViewPort;
    private GameState gameState;

    private void init() {
        GameStateGenerator gameStateGenerator = new GameStateGenerator();
        gameState = gameStateGenerator.generateImbroglioGameState();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

        TilesetResource tileSet = CP437TilesetResources.mdCurses16x16();

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth() - 5;
        int windowHeight = ((int) screenHeight) / tileSet.getHeight() - 5;

        int xOffset = 0;
        int topOffSet = 1;
        int bottomOffSet = 1;
        Position vOffset = Positions.create(xOffset, topOffSet);
        Size vDimensions = Sizes.create(windowWidth - (xOffset * 2), windowHeight - (topOffSet + bottomOffSet));
        mapViewPort = new ViewPort(vOffset, vDimensions);

        actionHandler = new GameActionHandler();
        tileRenderer = new GameWorldRenderer();

        // Begin Displaying TileGrid
        tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withTitle("Imbroglio")
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
        tileRenderer.renderImbroglioGame(tileGrid, gameState, mapViewPort);

        while (!gameState.isGameOver()) {
//            PlayerAction nextAction = inputHandler.consumeNextAction();
            PlayerAction nextAction = PlayerAction.UNKNOWN;
            if (nextAction != PlayerAction.UNKNOWN) {
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderImbroglioGame(tileGrid, gameState, mapViewPort);
            }
        }
        // TODO: show a game over screen, based on the GameState.
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
       ImbroglioGame game = new ImbroglioGame();
       game.init();
       game.update();
    }
}
