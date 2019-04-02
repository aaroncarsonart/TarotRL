package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameSprite;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

public class ZirconTileRenderer {

    private ZirconGameMapColorProfile colors;
    private GameMap renderedGameMap;
    private Tile[][] mapTiles;

    public ZirconTileRenderer() {
        colors = ZirconGameMapColorProfile.FOREST_GREEN;
    }

    /**
     * For now, draw the active game map onto the tile grid.
     * @param tileGrid
     * @param gameState
     */
    public void renderGameState(TileGrid tileGrid, GameState gameState) {

        // Create the full tileset for the active map, if needed.
        GameMap activeGameMap = gameState.getActiveMap();
        if (renderedGameMap != activeGameMap) {
            mapTiles = createMapTiles(activeGameMap);
            renderedGameMap = activeGameMap;
        }

        int gridWidth = tileGrid.getWidth();
        int gridHeight = tileGrid.getHeight();

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                tileGrid.setTileAt(
                        Positions.create(x, y),
                        mapTiles[y][x].createCopy());
            }
        }


        // draw sprites
        int px = gameState.getPlayerPosX();
        int py = gameState.getPlayerPosY();

        Tile underlyingTile = mapTiles[py][px];
        TileColor bgColor = underlyingTile.getBackgroundColor();
        TileColor playerColor = colors.player;
        char playerSprite = GameSprite.PLAYER;

        tileGrid.setTileAt(
                Positions.create(px, py),
                Tiles.newBuilder()
                        .withBackgroundColor(bgColor)
                        .withForegroundColor(playerColor)
                        .withCharacter(playerSprite)
                        .build());
    }

    public Tile[][] createMapTiles(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        mapTiles = new Tile[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                char mapTileCharacter = gameMap.getTile(y, x);

                TileColor fgColor, bgColor;
                if (mapTileCharacter == GameSprite.WALL) {
                    bgColor = colors.wallBG;
                    fgColor = colors.wallFG;
                } else if (mapTileCharacter == GameSprite.PATH) {
                    bgColor = colors.pathBG;
                    fgColor = colors.pathFG;
                } else {
                    bgColor = colors.defaultBG;
                    fgColor = colors.defaultFG;
                }

                mapTiles[y][x] = Tiles.newBuilder()
                        .withBackgroundColor(bgColor)
                        .withForegroundColor(fgColor)
                        .withCharacter(mapTileCharacter)
                        .build();
            }
        }
        return mapTiles;

    }
}
