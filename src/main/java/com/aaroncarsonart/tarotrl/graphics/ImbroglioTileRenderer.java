package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.List;

public class ImbroglioTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(MapTileRenderer.class);

    private ViewPort mapViewPort;

    private void initMapViewPort(TileGrid tileGrid) {
        int windowWidth = tileGrid.getWidth();
        int windowHeight = tileGrid.getHeight();

        int xOffset = 20;
        int topOffSet = 1;
        int bottomOffSet = 10;
        Position vOffset = Positions.create(xOffset, topOffSet);

        int vWidth = windowWidth - (xOffset) - 2;
        int vHeight = windowHeight - (topOffSet + bottomOffSet);

        Size vDimensions = Sizes.create(vWidth, vHeight);
        mapViewPort = new ViewPort(vOffset, vDimensions);
    }


    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        if (mapViewPort == null) {
            initMapViewPort(tileGrid);
        }
        renderImbroglioStatus(tileGrid, gameState);
    }

    private void renderTarotRLGame(TileGrid tileGrid, GameState gameState, ViewPort viewPort) {
        renderGameMapThroughViewPort(tileGrid, gameState, viewPort, true);
    }

    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort,
                                             boolean drawViewportBorder) {
        GameMap world = gameState.getActiveGameMap();

        // ensure coordinate spaces of viewport fit on the TileGrid.
        checkCoordinatesFit(tileGrid, viewPort);

        // For each tile in the viewport, draw the appropriate tile
        // on the TileGrid, applying the appropriate offset.
        // Offset is based on the camera heuristic.

        // camera coordinates
        Position3D camera = world.getCamera();
        int cx = camera.x;
        int cy = camera.y;

        int offsetX = viewPort.width / 2;
        int offsetY = viewPort.height / 2;

        for (int vx = 0; vx < viewPort.width; vx++) {
            for (int vy = 0; vy < viewPort.height; vy++) {
                // map coordinates
                int mx = cx - offsetX + vx;
                int my = cy - offsetY + vy;
                Position3D mapPos = new Position3D(mx, my, camera.z);

                // screen coordinates
                int sx = viewPort.x + vx;
                int sy = viewPort.y + vy;

                MapVoxel voxel = world.getVoxel(mapPos);
                TileType tileType = voxel.getTileType();
                if (tileType == TileType.EMPTY) {
                    tileType = gameState.getUndefinedTileType();
                }

                TileDefinition tileDefinition = tileType.getMetadata();
                Tile tile = createZirconTileFrom(tileDefinition);
                tileGrid.setTileAt(Positions.create(sx, sy), tile);
            }
        }

        // draw entities
        for (int vx = 0; vx < viewPort.width; vx++) {
            for (int vy = 0; vy < viewPort.height; vy++) {
                // map coordinates
                int mx = cx - offsetX + vx;
                int my = cy - offsetY + vy;
                Position3D mapPos = new Position3D(mx, my, camera.z);

                MapEntity entity = world.getEntity(mapPos);
                if (entity != null) {

                    // screen coordinates
                    int sx = viewPort.x + vx;
                    int sy = viewPort.y + vy;

                    TileDefinition tileDefinition = entity.getTileDefinition();
                    Tile tile = createZirconTileFrom(tileDefinition);
                    tileGrid.setTileAt(Positions.create(sx, sy), tile);
                }
            }
        }

        // draw player

        // tile grid entity offsets
        int entityOffsetX = viewPort.x - cx + offsetX;
        int entityOffsetY = viewPort.y - cy + offsetY;

        // screen player voxelPosition
        int spx = camera.x + entityOffsetX;
        int spy = camera.y + entityOffsetY;

        TileDefinition playerTile = TileType.PLAYER.getMetadata();
        tileGrid.setTileAt(
                Positions.create(spx, spy),
                createZirconTileFrom(playerTile));

//      writeText(tileGrid, "█ ▄ ▌▐ ▀", 40, 10);

        if (drawViewportBorder) {
            drawSimpleBorder(tileGrid, viewPort, true);
        }
    }


    private void writeSingleLogStatus(TileGrid tileGrid,
                                      GameState gameState,
                                      ViewPort logViewPort){
        int availableLogRows = logViewPort.height - 1;

        int sx = logViewPort.x + 1;
        int sy = logViewPort.y + 1;

        // clear the old status.
        int maxWidth = logViewPort.width - 1;
        String empty = TextUtils.getStringOfLength(' ', maxWidth);

        for (int i = 0; i < availableLogRows ; i++) {
            writeText(tileGrid, empty, sx, sy + i);
        }
        String status = gameState.getStatus();
        if (StringUtils.isNotBlank(status)) {
            List<String> wrappedStatus = TextUtils.getWordWrappedText(status, maxWidth);

            for (int i = 0; i < Math.min(availableLogRows, wrappedStatus.size()); i++) {
                String nextStatus = wrappedStatus.get(i);
                writeText(tileGrid, nextStatus, sx, sy + i);
            }
        }
    }


    protected void renderImbroglioStatus(TileGrid tileGrid, GameState gameState) {
        int width = tileGrid.getWidth();
        int height = tileGrid.getHeight();

        // top left
        String status = gameState.getStatus();
        if (status == null) {
            status = "";
        }

        int sx = 0;
        int sy = 0;

        String statusMsg = String.format("%-" + width + "s", status);
        writeText(tileGrid, statusMsg, sx, sy, GameColors.LIGHT_GRAY, GameColors.DARKER_GRAY);

        // top right
        int level = 1 - gameState.getActiveGameMap().getCamera().z;
        String levelMsg = "Level " + level;

        sx = width - levelMsg.length();
        sy = 0;

        writeText(tileGrid, levelMsg, sx, sy, GameColors.LIGHT_GRAY, GameColors.DARKER_GRAY);

        // bottom left
        int collectedCount = gameState.getPlayerItems().size();
        String magicMsg = String.format("Remaining magic: %-" + width + "s", collectedCount);

        sx = 0;
        sy = height - 1;

        writeText(tileGrid, magicMsg, sx, sy, GameColors.MAGENTA, GameColors.DARKER_GRAY);

        // bottom right
        int stepsRemaining = gameState.getTreasure() - gameState.getStepCount();
        String stepsMsg = "Remaining steps: " + stepsRemaining;

        sx = width - stepsMsg.length();
        sy = height - 1;
        writeText(tileGrid, stepsMsg, sx, sy, GameColors.GREEN, GameColors.DARKER_GRAY);
    }

}
