package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GameWorldRenderer;
import com.aaroncarsonart.tarotrl.graphics.TileRenderer;
import com.aaroncarsonart.tarotrl.graphics.ViewPort;
import com.aaroncarsonart.tarotrl.input.InputHandler;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.util.Loggable;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Region3D;
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
 * The main class for encapsulating all TarotRL game logic.
 */
public class TarotRLGame implements Loggable {

    private TileRenderer tileRenderer;
    private GameActionHandler actionHandler;
    private InputHandler inputHandler;
    private TileGrid tileGrid;
    private ViewPort mapViewPort;
    private GameState gameState;

    private void start() {
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

        inputHandler = new InputHandler();
        inputHandler.listenForInput(tileGrid, gameState);
        inputHandler.addPlayerActionListener(this::update);

        tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);
    }

    private void printGameWorldInfo() {
        GameWorld world = gameState.getGameWorld();
        int voxelCount = gameState.getGameWorld().getWorldMap().size();

        Region3D worldRegion = world.calculateBoundingRegion3D();
        int regionVolume = worldRegion.volume();

        debug(String.format("WorldMap dimensions: %s\n", worldRegion));
        debug(String.format("WorldMap bounding region volume:  %,d voxels\n", regionVolume));
        debug(String.format("worldMap size: %,d voxels\n", voxelCount));
    }

    private void update(PlayerAction nextAction) {
        if (nextAction != PlayerAction.UNKNOWN) {
            actionHandler.processPlayerAction(nextAction, gameState);
            tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);
        }
        // TODO: show a game over screen, based on the GameState.
        if (gameState.isGameOver()) {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        TarotRLGame game = new TarotRLGame();
        game.start();
    }
}
