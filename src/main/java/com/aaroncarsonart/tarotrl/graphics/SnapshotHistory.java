package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameModeComponents;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.input.InputHandler;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.Layers;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Cell;
import org.hexworks.zircon.api.data.Snapshot;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.DrawSurface;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.UIEventResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SnapshotHistory implements InputHandler, GameController, TileRenderer, Observer {
    private static final Logger LOG = new Logger(SnapshotHistory.class);

    public static final String KEY_PREVIOUS_SNAPSHOT = "[";
    public static final String KEY_NEXT_SNAPSHOT = "]";

    private GameTileRenderer tileRenderer = new GameTileRenderer();

    private List<Snapshot> snapshots = new ArrayList<>();
    private int snapshotIndex = 0;
    private int snapshotsMaxSize = 101;
    boolean snapshotMode = false;
    private SnapshotAction action;

    public void takeSnapshot(DrawSurface drawSurface) {
        LOG.trace("take snapshot");
        Snapshot snapshot = drawSurface.createSnapshot();
        snapshots.add(0, snapshot);
        if (snapshots.size() > snapshotsMaxSize) {
            snapshots.remove(snapshotsMaxSize);
        }

        // reset to current snapshot.
        //snapshotIndex = 0;
    }

    public void previousIndex() {
        LOG.trace("previous index");
        if (snapshotIndex < (snapshots.size() - 1)) {
            snapshotIndex += 1;
        }
    }

    public void next() {
        LOG.trace("next index");
        if (snapshotIndex > 0) {
            snapshotIndex -= 1;
        }
    }

    public void first() {
        snapshotIndex = 0;
    }

    public void drawSnapshot(TileGrid tileGrid) {
        LOG.trace("snapshot count %d", snapshots.size());
        LOG.trace("draw snapshot %d", snapshotIndex);
        Snapshot snapshot = snapshots.get(snapshotIndex);
        for (Cell cell : snapshot.component1()) {
            tileGrid.setTileAt(cell.getPosition(), cell.getTile());
        }
    }

    public void displayText(TileGrid tileGrid, String displayText) {
        int tx = tileGrid.getWidth() / 2 - displayText.length() / 2;
        int ty = 3;

        int width = displayText.length();
        int height = (0 < snapshotIndex && snapshotIndex < snapshots.size() - 1) ? 1 : 2;

        TileColor modalColor = TileColors.create(0, 0, 255, 64);
        TileColor modalTextColor = TileColors.create(128, 128, 64);

        Tile bgModalTile = Tiles.newBuilder()
                .withCharacter(' ')
                .withForegroundColor(modalColor)
                .withBackgroundColor(modalColor)
                .build();

        // draw modal background square
        int mx = tx - 1;
        int my = ty - 1;
        int mwidth = tx + width + 1;
        int mheight = ty + height + 1;
        drawBox(layer, Tiles.empty(), mx, my, mwidth, mheight + 1);
        drawBox(layer, bgModalTile, mx, my, mwidth, mheight);




        tileRenderer.writeText(layer, displayText, tx, ty, modalTextColor, modalColor);

        if (snapshotMode && snapshotIndex == 0) {
            displayText = "(Current)";
            int nx = tileGrid.getWidth() / 2 - displayText.length() / 2;
            int ny = ty + 1;
            tileRenderer.writeText(layer, displayText, nx, ny, modalTextColor, modalColor);
        } else if (snapshotMode && snapshotIndex == snapshots.size() - 1) {
            displayText = "(Oldest)";
            int nx = tileGrid.getWidth() / 2 - displayText.length() / 2;
            int ny = ty + 1;
            tileRenderer.writeText(layer, displayText, nx, ny, modalTextColor, modalColor);
        }
    }

    /**
     * Implement {@link Observer} contract on {@link GraphicsContext}
     * to take a snapshot of the {@link TileGrid}
     *
     * @param observable The Observable that triggered this callback.
     * @param arg The GraphicsContext being observed.
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (!snapshotMode && arg instanceof TileGrid) {
            TileGrid tileGrid = (TileGrid) arg;
            takeSnapshot(tileGrid);
        }
    }

    private enum SnapshotAction {
        PREVIOUS,
        NEXT,
        EXIT
    }

    @Override
    public UIEventResponse handleKeyPressed(KeyboardEvent event, UserInput userInput) {
        LOG.trace("handleKeyTyped");
        if (event.getKey().equals(KEY_PREVIOUS_SNAPSHOT)) {
            action = SnapshotAction.PREVIOUS;
            //userInput.setCurrentAction(SnapshotAction.PREVIOUS);
            userInput.doNotifyAll();
            return UIEventResponses.processed();
        } else if (event.getKey().equals(KEY_NEXT_SNAPSHOT)) {
            action = SnapshotAction.NEXT;
            //userInput.setCurrentAction(SnapshotAction.NEXT);
            userInput.doNotifyAll();
            return UIEventResponses.processed();
        } else {
            action = SnapshotAction.EXIT;
            //userInput.setCurrentAction(SnapshotAction.EXIT);
            userInput.doNotifyAll();
            return UIEventResponses.processed();
        }
    }

    @Override
    public UIEventResponse handleKeyTyped(KeyboardEvent event, UserInput userInput) {
        return UIEventResponses.pass();
    }

    @Override
    public UIEventResponse handleKeyReleased(KeyboardEvent event, UserInput userInput) {
        return UIEventResponses.pass();
    }

    @Override
    public boolean update(GameState state, UserInput input) {
        LOG.trace("update");
        if (action == SnapshotAction.PREVIOUS) {
            snapshotMode = true;
            previousIndex();
            return true;
        } else if (action == SnapshotAction.NEXT) {
            snapshotMode = true;
            next();
            return true;
        }
        // Clean up.
        else if (action == SnapshotAction.EXIT) {
            snapshotMode = false;
            first();
            state.setGameMode(GameMode.MAP_NAVIGATION);
            return true;
        }
        return false;
    }

    private Layer layer;

    private void initLayer(TileGrid tileGrid) {
        layer = Layers.newBuilder()
                .withSize(tileGrid.getSize())
                .build();
        tileGrid.pushLayer(layer);
    }

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        if (layer == null) {
            initLayer(tileGrid);
        }
        LOG.trace("render");
        drawSnapshot(tileGrid);
        displayText(tileGrid, "Viewing Snapshot: " + snapshotIndex);
    }

    public GameModeComponents asGameModeComponents() {
        return new GameModeComponents(this, this, this);
    }
}
