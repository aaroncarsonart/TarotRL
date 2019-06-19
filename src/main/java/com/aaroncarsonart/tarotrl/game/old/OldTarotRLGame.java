package com.aaroncarsonart.tarotrl.game.old;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.generator.GameStateGenerator;
import com.aaroncarsonart.tarotrl.graphics.GameWorldRenderer;
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
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.data.Cell;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Snapshot;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;
import org.hexworks.zircon.api.uievent.KeyboardEventType;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class for encapsulating all TarotRL game logic.
 */
public class OldTarotRLGame {
    private static final Logger LOG = new Logger(OldTarotRLGame.class);

    private GameWorldRenderer tileRenderer;
    private PlayerActionController actionHandler;
    private InputEventController playerInputController;
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

        actionHandler = new PlayerActionController();
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

        playerInputController = new InputEventController();
        playerInputController.listenForInputs(tileGrid, gameState);
        playerInputController.addPlayerActionListener(this::update);

        gameState.setGameMode(GameMode.MAP_NAVIGATION);
        tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);

        takeSnapshot();


        tileGrid.onKeyboardEvent(KeyboardEventType.KEY_TYPED, (event, phase) -> {
            boolean drawSnapshot = false;
            if (event.getKey().equals("[")) {
                if (snapshotPosition < (snapshots.size() - 1)) {
                    snapshotPosition += 1;
                }
                drawSnapshot = true;
            } else if (event.getKey().equals("]")) {
                if (snapshotPosition > 0) {
                    snapshotPosition -= 1;
                }
                drawSnapshot = true;
            }

            if (drawSnapshot) {
                Snapshot snapshot = snapshots.get(snapshotPosition);
                for (Cell cell : snapshot.component1()) {
                    tileGrid.setTileAt(cell.getPosition(), cell.getTile());
                }
                tileGrid.write("Viewing Snapshot " + 1, Positions.create(0, 0));
            }
            return UIEventResponses.processed();
        });

    }

    /** Dumb little instance variable cached here, used only for log. NO APOLOGIES. **/
    private int updateCount = 0;


    private List<Snapshot> snapshots = new ArrayList<>();
    private int snapshotPosition = 0;
    private int snapshotsMaxSize = 100;

    private void takeSnapshot() {
        Snapshot snapshot = tileGrid.createSnapshot();
        snapshots.add(0, snapshot);
        if (snapshots.size() > snapshotsMaxSize) {
            snapshots.remove(snapshotsMaxSize);
        }

    }



    private void update(PlayerAction nextAction) {
        updateCount++;
        LOG.trace("update() %d", updateCount);
        if (nextAction != PlayerAction.UNKNOWN) {
            LOG.debug("process PlayerAction.%s", nextAction);
            // update player state
            actionHandler.processPlayerAction(nextAction, gameState);

            // update rest of game state
            // something like:
            // gameState.update();

            // draw game to screen
            tileRenderer.renderTarotRLGame(tileGrid, gameState, mapViewPort);

            snapshotPosition = 0;
            takeSnapshot();
        }
        // TODO: show a game over screen, based on the GameState.
        if (gameState.isGameOver()) {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
        OldTarotRLGame game = new OldTarotRLGame();
        game.start();
    }
}
