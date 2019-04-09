package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.WorldVoxel;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.Map;

/**
 * Encapsulate special logic for rendering GameWorlds
 * to the TileGrid display.
 */
public class GameWorldRenderer extends TileRenderer {

    @Override
    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort) {
        GameWorld world = gameState.getGameWorld();

        Map<Character, TileDefinition> tileSprites = gameState.getTileDefinitions();

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
                char sprite = tileType.getSprite();
                TileDefinition tileDefinition = tileSprites.get(sprite);
                Tile tile = createZirconTileFrom(tileDefinition);
                tileGrid.setTileAt(Positions.create(sx, sy), tile);
            }
        }

        // draw entities

        // tile grid entity offsets
        int entityOffsetX = viewPort.x - cx + offsetX;
        int entityOffsetY = viewPort.y - cy + offsetY;

        // screen player voxelPosition
        int spx = camera.x + entityOffsetX;
        int spy = camera.y + entityOffsetY;

        TileDefinition playerTile = tileSprites.get(TileType.PLAYER.getSprite());
        tileGrid.setTileAt(
                Positions.create(spx, spy),
                createZirconTileFrom(playerTile));

        drawSimpleBorder(tileGrid, viewPort, true);
    }
}
