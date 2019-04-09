package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.Symbols;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TileRenderer {

    private GameColors colors;
    private GameMap renderedGameMap;
    private Tile[][] mapTiles;
    private Tile outOfBoundsTile;

    public TileRenderer() {
        colors = GameColors.FOREST_GREEN;
    }

    private void calculateOutOfBoundsTile() {
        char outOfBoundsSprite = renderedGameMap.getOutOfBoundsTile();
        TileDefinition definition = renderedGameMap.getTileSprites().get(outOfBoundsSprite);
        if (definition == null) {
            definition = renderedGameMap.getTileSprites().get('#');
        }
        outOfBoundsTile = Tiles.newBuilder()
                .withBackgroundColor(definition.getBackgroundColor())
                .withForegroundColor(definition.getForegroundColor())
                .withCharacter(definition.getDisplaySprite())
                .build();
    }

    /**
     * For now, draw the active game map onto the tile grid.
     *
     * @param tileGrid  The TileGrid to draw to
     * @param gameState The GameState to draw from
     * @param viewPort  The ViewPort to use
     */
    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort) {

        // Create the full tileset for the active map, if needed.
        GameMap activeGameMap = gameState.getActiveMap();
        if (renderedGameMap != activeGameMap) {
            mapTiles = createMapTiles(activeGameMap);
            renderedGameMap = activeGameMap;
            calculateOutOfBoundsTile();
        }

        List<Position2D> dirtyTiles = activeGameMap.getDirtyTiles();
        if (!dirtyTiles.isEmpty()) {
            for(Position2D pos : dirtyTiles) {
                TileDefinition updated = renderedGameMap.getTileDefinition(pos);
                mapTiles[pos.y()][pos.x()] = createZirconTileFrom(updated);
            }
        }


        // ensure coordinate spaces of viewport fit on the TileGrid.
        checkCoordinatesFit(tileGrid, viewPort);

        // For each tile in the viewport, draw the appropriate tile
        // on the TileGrid, applying the appropriate offset.
        // Offset is based on the camera heuristic.

        // camera coordinates
        int cx = gameState.getPlayerPosition().x();
        int cy = gameState.getPlayerPosition().y();

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
                    tile = outOfBoundsTile;
                }
                tileGrid.setTileAt(Positions.create(sx, sy), tile);
            }
        }

        // draw entities

        // tile grid entity offsets
        int entityOffsetX = viewPort.x - cx + offsetX;
        int entityOffsetY = viewPort.y - cy + offsetY;

        // screen player voxelPosition
        int spx = gameState.getPlayerPosition().x() + entityOffsetX;
        int spy = gameState.getPlayerPosition().y() + entityOffsetY;

        TileDefinition playerTile = activeGameMap.getTileSprites().get(TileType.PLAYER.getSprite());
        tileGrid.setTileAt(
                Positions.create(spx, spy),
                createZirconTileFrom(playerTile));

        drawSimpleBorder(tileGrid, viewPort, true);
    }

    public void checkCoordinatesFit(TileGrid tileGrid, ViewPort viewPort) {
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
                    " has DIMENSIONS that don't fit on TileGrid with {" +
                    "width = " + gridWidth + ", " +
                    "height = " + gridWidth + ", " +
                    "} ";
            throw new TarotRLException(errorMessage);
        }

    }

    public void drawSimpleBorder(TileGrid tileGrid, ViewPort viewPort, boolean decorateCorners) {
        if (decorateCorners) {
            drawDecoratedCorners(tileGrid, viewPort);
            drawBorderLines(tileGrid, viewPort, 1);
        } else {
            drawSimpleCorners(tileGrid, viewPort);
            drawBorderLines(tileGrid, viewPort, 0);
        }
    }

    public void drawBorderLines(TileGrid tileGrid, ViewPort viewPort, int cornerOffset) {
        TileColor bg = GameColors.BLACK;
        TileColor fg = GameColors.WHITE;

        int startOffset = 1 + cornerOffset;
        int endOffset = cornerOffset;

        // draw top horizontal line
        for (int x = viewPort.x + startOffset; x < viewPort.x + viewPort.width - endOffset; x++) {
            int y = viewPort.y;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_HORIZONTAL)
                    .build());
        }

        // draw bottom horizontal line
        for (int x = viewPort.x + startOffset; x < viewPort.x + viewPort.width - endOffset; x++) {
            int y = viewPort.y + viewPort.height;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_HORIZONTAL)
                    .build());
        }

        // draw left vertical line
        for (int y = viewPort.y + startOffset; y < viewPort.y + viewPort.height - endOffset; y++) {
            int x = viewPort.x;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_VERTICAL)
                    .build());
        }

        // draw right vertical line
        for (int y = viewPort.y + startOffset; y < viewPort.y + viewPort.height - endOffset; y++) {
            int x = viewPort.x + viewPort.width;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_VERTICAL)
                    .build());
        }
    }

    private void drawSimpleCorners(TileGrid tileGrid, ViewPort viewport) {
        TileColor bg = GameColors.BLACK;
        TileColor fg = GameColors.WHITE;

        Position topLeft = Positions.create(viewport.x, viewport.y);
        tileGrid.setTileAt(topLeft, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_TOP_LEFT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_TOP_LEFT_CORNER)
                .build());

        Position topRight = Positions.create(viewport.x + viewport.width, viewport.y);
        tileGrid.setTileAt(topRight, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_TOP_RIGHT_CORNER)
                .build());

        Position bottomLeft = Positions.create(viewport.x, viewport.y + viewport.height);
        tileGrid.setTileAt(bottomLeft, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER)
                .build());

        Position bottomRight = Positions.create(viewport.x + viewport.width, viewport.y + viewport.height);
        tileGrid.setTileAt(bottomRight, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER)
                .build());
}

    private void drawDecoratedCorners(TileGrid tileGrid, ViewPort viewport) {
        Tile swords = Tiles.newBuilder()
                .withCharacter(Symbols.SPADES)
                .withForegroundColor(GameColors.BLUE)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile cups = Tiles.newBuilder()
                .withCharacter(Symbols.HEART)
                .withForegroundColor(GameColors.GREEN)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile wands = Tiles.newBuilder()
                .withCharacter(Symbols.CLUB)
                .withForegroundColor(GameColors.RED)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile pentacles = Tiles.newBuilder()
                .withCharacter(Symbols.DIAMOND)
                .withForegroundColor(GameColors.YELLOW)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Position topLeftCorner = Positions.create(viewport.x, viewport.y);
        Position topRightCorner = Positions.create(viewport.x + viewport.width, viewport.y);
        Position bottomLeftCorner = Positions.create(viewport.x, viewport.y + viewport.height);
        Position bottomRightCorner = Positions.create(viewport.x + viewport.width, viewport.y + viewport.height);

        tileGrid.setTileAt(topLeftCorner, swords);
        tileGrid.setTileAt(topRightCorner, cups);
        tileGrid.setTileAt(bottomLeftCorner, pentacles);
        tileGrid.setTileAt(bottomRightCorner, wands);

        List<Position> corners = Arrays.asList(
                topLeftCorner,
                topRightCorner,
                bottomLeftCorner,
                bottomRightCorner);

        // draw a box around each corner, for fun.
        for (Position corner : corners) {

            Position left = corner.withRelative(Positions.create(-1, 0));
            Position right = corner.withRelative(Positions.create(1, 0));
            Position top = corner.withRelative(Positions.create(0, -1));
            Position bottom = corner.withRelative(Positions.create(0, 1));
            Position topLeft = corner.withRelative(Positions.create(-1, -1));
            Position topRight = corner.withRelative(Positions.create(1, -1));
            Position bottomLeft = corner.withRelative(Positions.create(-1, 1));
            Position bottomRight = corner.withRelative(Positions.create(1, 1));

            List<Position> boxPositions = Arrays.asList(
                    topLeft, top, topRight,
                    left, right,
                    bottomLeft, bottom, bottomRight);

            String boxSymbols = "╔═╗║║╚═╝";

            for (int i = 0; i < 8; i++) {
                char nextSymbol = boxSymbols.charAt(i);
                Position nextPosition = boxPositions.get(i);

                tileGrid.setTileAt(nextPosition, Tiles.newBuilder()
                        .withBackgroundColor(GameColors.BLACK)
                        .withForegroundColor(GameColors.WHITE)
                        .withCharacter(nextSymbol)
                        .build());
            }
        }
    }

    public void drawGuiTextInfo(TileGrid tileGrid,
                                GameState gameState,
                                ViewPort mapViewPort){
        // draw player info
        int vw = 18;
        int vh = 10;
        ViewPort infoViewPort = new ViewPort(0, 0, vw, vh);
//        drawSimpleBorder(tileGrid, infoViewPort, false);

        Position3D camera = gameState.getGameWorld().getCamera();
        int px = camera.x;
        int py = camera.y;
        int pz = camera.z;
        String playerCoordinates = "Pos: " + px + ", "+  py + ", " + pz;
        writeText(tileGrid, playerCoordinates, 1, 1);

        String gameTurns = "turns: " +  gameState.getTurnCounter();;
        writeText(tileGrid, gameTurns, 1, 3);

        String format = "%-" + (vw - 1) + "s";

        writeText(tileGrid, "last action:", 1, 5);
        String previousAction = String.format(format, gameState.getPreviousAction().name());
        writeText(tileGrid, previousAction , 1, 6);

        writeText(tileGrid, "current action:", 1, 8);
        String currentAction = String.format(format, gameState.getCurrentAction().name());
        writeText(tileGrid, currentAction , 1, 9);

        String devMode = String.format(format, "devMode: " + gameState.isDevMode());
        writeText(tileGrid, devMode , 1, 11);

        String shiftDown = String.format(format, "shiftDown: " + gameState.isShiftDown());
        writeText(tileGrid, shiftDown , 1, 13);


        // draw status log
        int lx = mapViewPort.x;
        int ly = mapViewPort.y + mapViewPort.height + 2;
        int lw = mapViewPort.width;
        int lh = tileGrid.getHeight() - (ly + 1);

        ViewPort logViewPort = new ViewPort(lx, ly, lw, lh);
        drawSimpleBorder(tileGrid, logViewPort, false);
        writeSingleLogStatus(tileGrid, gameState, logViewPort);
    }

    private String getStringOfLength(char c, int length) {
        return IntStream.range(0, length)
                .mapToObj(j -> String.valueOf(c))
                .collect(Collectors.joining());
    }

    private void writeSingleLogStatus(TileGrid tileGrid,
                                       GameState gameState,
                                       ViewPort logViewPort){
        int availableLogRows = logViewPort.height - 1;

        int sx = logViewPort.x + 1;
        int sy = logViewPort.y + 1;

        // clear the old status.
        int maxWidth = logViewPort.width - 1;
        String empty = getStringOfLength(' ', maxWidth);

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

    private void writeRollingLogStatus(TileGrid tileGrid,
                                       GameState gameState,
                                       ViewPort logViewPort){
        int availableLogRows = logViewPort.height - 1;

        List<String> statusLog = gameState.getStatusLog();
        int logSize = statusLog.size();
        for (int n = 1; n <= availableLogRows; n++) {
            int statusIndex = logSize - n;
            if (statusIndex < 0) break;

            int logRowPosition = availableLogRows - n;

            int sx = logViewPort.x + 1;
            int sy = logViewPort.y + 1 + logRowPosition;

            // clear the old status.
            String empty = getStringOfLength(' ', logViewPort.width - 2);
            writeText(tileGrid, empty, sx, sy);

            String status = statusLog.get(statusIndex);
            writeText(tileGrid, status, sx, sy);
        }
    }

    public void writeText(TileGrid tileGrid, String text, int x, int y) {
        for (int cursor = 0; cursor < text.length(); cursor++) {
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

        Map<Character, TileDefinition> tileMetadata = gameMap.getTileSprites();

        mapTiles = new Tile[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                char mapTileCharacter = gameMap.getTile(y, x);
                TileDefinition tile = tileMetadata.get(mapTileCharacter);
                mapTiles[y][x] = createZirconTileFrom(tile);
            }
        }
        return mapTiles;
    }

    public Tile createZirconTileFrom(TileDefinition definition) {
        TileColor bgColor = definition.getBackgroundColor();
        TileColor fgColor = definition.getForegroundColor();
        char displaySprite = definition.getDisplaySprite();

        return Tiles.newBuilder()
                .withBackgroundColor(bgColor)
                .withForegroundColor(fgColor)
                .withCharacter(displaySprite)
                .build();
    }
}
