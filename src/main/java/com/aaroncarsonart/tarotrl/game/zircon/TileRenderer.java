package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameSprite;
import com.aaroncarsonart.tarotrl.map.GameTile;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

public class TileRenderer {

    private GameColors colors;
    private GameMap renderedGameMap;
    private Tile[][] mapTiles;

    public TileRenderer() {
        colors = GameColors.FOREST_GREEN;
    }

    /**
     * For now, draw the active game map onto the tile grid.
     * @param tileGrid
     * @param gameState
     */
    public void renderEntireGameMap(TileGrid tileGrid, GameState gameState) {

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

    /**
     * For now, draw the active game map onto the tile grid.
     * @param tileGrid The TileGrid to draw to
     * @param gameState The GameState to draw from
     * @param viewPort The ViewPort to use
     */
    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort) {

        // Create the full tileset for the active map, if needed.
        GameMap activeGameMap = gameState.getActiveMap();
        if (renderedGameMap != activeGameMap) {
            mapTiles = createMapTiles(activeGameMap);
            renderedGameMap = activeGameMap;
        }

        int gridWidth = tileGrid.getWidth();
        int gridHeight = tileGrid.getHeight();

        // ensure coordinate spaces of viewport fit on the TileGrid.
        if (gridWidth < (viewPort.x + viewPort.width) ||
                gridHeight < (viewPort.y + viewPort.height)) {
            String errorMessage = "Viewport with {" +
                    "x = " + viewPort.x + ", " +
                    "y = " + viewPort.y + ", " +
                    "width = " + viewPort.width + ", " +
                    "height = " + viewPort.height +
                    "} " +
                    " has dimensions that don't fit on TileGrid with {" +
                    "width = " + gridWidth + ", " +
                    "height = " + gridWidth + ", " +
                    "} ";
            throw new TarotRLException(errorMessage);
        }

        // For each tile in the viewport, draw the appropriate tile
        // on the TileGrid, applying the appropriate offset.
        // Offset is based on the camera heuristic.

        // camera coordinates
        int cx = gameState.getPlayerPosX();
        int cy = gameState.getPlayerPosY();
        Position2D camera = new Position2D(cx, cy);

        int offsetX = viewPort.width / 2;
        int offsetY = viewPort.height / 2;

        for (int vx = 0; vx < viewPort.width; vx++) {
            for (int vy = 0; vy < viewPort.height; vy++) {
                // map coordinates
                int mx = cx - offsetX + vx;
                int my = cy - offsetY + vy;
                Position2D mapPos = new Position2D(mx, my);

                // screen coordinates
                int sx = viewPort.x + vx;
                int sy = viewPort.y + vy;

                Tile tile;
                if (activeGameMap.withinBounds(mapPos)) {
                    tile = mapTiles[my][mx].createCopy();
                } else {
                    tile = GameTile.WALL.getTile();
                }
                tileGrid.setTileAt(Positions.create(sx, sy), tile);
            }
        }

        // draw entities

        // tile grid entity offsets
        int entityOffsetX = viewPort.x - cx + offsetX;
        int entityOffsetY = viewPort.y - cy + offsetY;

        // screen player position
        int spx = gameState.getPlayerPosX() + entityOffsetX;
        int spy = gameState.getPlayerPosY() + entityOffsetY;

        tileGrid.setTileAt(
                Positions.create(spx, spy),
                GameTile.PLAYER.getTile());
    }

    public void drawGuiTextInfo(TileGrid tileGrid,
                                GameState gameState,
                                ViewPort viewPort){

        int px = gameState.getPlayerPosX();
        int py = gameState.getPlayerPosY();
        String playerCoordinates = "Position: " + px + ", "+  py + "    ";
        writeText(tileGrid, playerCoordinates, 1, 1);
        //tileGrid.write(playerCoordinates, Positions.create(2, 2));

        String gameTurns = "turns: " +  gameState.getTurnCounter();;
        writeText(tileGrid, gameTurns, 1, 3);
        System.out.println("BG: " + tileGrid.getBackgroundColor());
        System.out.println("FG: " + tileGrid.getForegroundColor());
    }

    public void writeText(TileGrid tileGrid, String text, int x, int y) {
        for (int cursor = 0; cursor < text.length(); cursor ++) {
            tileGrid.setTileAt(Positions.create(x + cursor, y),
                    Tiles.newBuilder()
                            .withCharacter(text.charAt(cursor))
                            .withBackgroundColor(tileGrid.getBackgroundColor())
                            .withForegroundColor(tileGrid.getForegroundColor())
                            .build());

        }
    }



    public Tile[][] createMapTiles(GameMap gameMap) {
        int width = gameMap.getWidth();
        int height = gameMap.getHeight();

        mapTiles = new Tile[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                char mapTileCharacter = gameMap.getTile(y, x);
                GameTile gameTile;
                 if (mapTileCharacter == GameTile.PATH.getCharacter()) {
                     gameTile = GameTile.PATH;
                } else if (mapTileCharacter == GameTile.WALL.getCharacter()) {
                     gameTile = GameTile.WALL;
                 } else {
                     gameTile = GameTile.EMPTY;
                }
                 mapTiles[y][x] = gameTile.getTile();

//                TileColor fgColor, bgColor;
//                if (mapTileCharacter == GameSprite.WALL) {
//                    bgColor = colors.bgWall;
//                    fgColor = colors.fgWall;
//                } else if (mapTileCharacter == GameSprite.PATH) {
//                    bgColor = colors.bgOpen;
//                    fgColor = colors.fgOpen;
//                } else {
//                    bgColor = colors.bgEmpty;
//                    fgColor = colors.fgEmpty;
//                }
//
//                mapTiles[y][x] = Tiles.newBuilder()
//                        .withBackgroundColor(bgColor)
//                        .withForegroundColor(fgColor)
//                        .withCharacter(mapTileCharacter)
//                        .build();
            }
        }
        return mapTiles;

    }
}
