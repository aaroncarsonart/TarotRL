package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.entity.Entity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.WorldVoxel;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

/**
 * Encapsulate special logic for rendering GameWorlds
 * to the TileGrid display.
 */
public class GameWorldRenderer extends TileRenderer {

    public void renderTarotRLGame(TileGrid tileGrid, GameState gameState, ViewPort viewPort) {
        renderGameMapThroughViewPort(tileGrid, gameState, viewPort, true);
        drawGuiTextInfo(tileGrid, gameState, viewPort);
    }

    public void renderImbroglioGame(TileGrid tileGrid, GameState gameState, ViewPort viewPort) {
        renderGameMapThroughViewPort(tileGrid, gameState, viewPort, false);
        renderImbroglioStatus(tileGrid, gameState);
    }

    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort,
                                             boolean drawViewportBorder) {
        GameWorld world = gameState.getGameWorld();

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

                WorldVoxel voxel = world.getVoxel(mapPos);
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

                Entity entity = world.getEntity(mapPos);
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
}
