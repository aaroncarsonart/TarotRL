package com.aaroncarsonart.tarotrl.game.old;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GameWorldRenderer;
import com.aaroncarsonart.tarotrl.graphics.SnapshotHistory;
import com.aaroncarsonart.tarotrl.graphics.ViewPort;
import com.aaroncarsonart.tarotrl.input.InputEventController;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.util.Logger;
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
 * Play the game Imbroglio, with the classic colors and rules.
 */
public class OldImbroglioGame {
    private static final Logger LOG = new Logger(OldImbroglioGame.class);

    private GameWorldRenderer tileRenderer;
    private PlayerActionController actionHandler;
    private InputEventController playerInputController;
    private TileGrid tileGrid;
    private ViewPort mapViewPort;
    private GameState gameState;

    private void start() {
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

        actionHandler = new PlayerActionController();
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

        playerInputController = new InputEventController();
        playerInputController.listenForInputs(tileGrid, gameState);
        playerInputController.addPlayerActionListener(this::update);

        gameState.setGameMode(GameMode.MAP_NAVIGATION);
        tileRenderer.renderImbroglioGame(tileGrid, gameState, mapViewPort);
    }

    /** Dumb little instance variable cached here, used only for log. NO APOLOGIES. **/
    private int updateCount = 0;

    private void update(PlayerAction nextAction) {
        LOG.trace("update()", ++updateCount);
        if (nextAction != PlayerAction.UNKNOWN) {
            LOG.debug("process PlayerAction.%s", nextAction);
            actionHandler.processPlayerAction(nextAction, gameState);
            tileRenderer.renderImbroglioGame(tileGrid, gameState, mapViewPort);
        }
        // TODO: show a game over screen, based on the GameState.
        if (gameState.isGameOver()) {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
       OldImbroglioGame game = new OldImbroglioGame();
       game.start();
    }
}
