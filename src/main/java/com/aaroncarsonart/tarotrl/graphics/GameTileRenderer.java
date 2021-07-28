package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.DrawSurface;
import org.hexworks.zircon.api.graphics.Symbols;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(GameTileRenderer.class);

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        writeText(tileGrid, "Hello, map!", 0, 0);
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
            LOG.error(errorMessage);
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

    public void drawSimpleCorners(TileGrid tileGrid, ViewPort viewport) {
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

    public void drawDecoratedCorners(TileGrid tileGrid, ViewPort viewport) {
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
        tileGrid.setTileAt(topRightCorner, wands);
        tileGrid.setTileAt(bottomLeftCorner, pentacles);
        tileGrid.setTileAt(bottomRightCorner, cups);

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

    public void renderTarotRLTextStatus(TileGrid tileGrid,
                                        GameState gameState,
                                        ViewPort mapViewPort){
        // draw player info
        int vw = 18;
        int vh = 10;
        ViewPort infoViewPort = new ViewPort(0, 0, vw, vh);
//        drawSimpleBorder(tileGrid, infoViewPort, false);

        Position3D camera = gameState.getActiveGameMap().getCamera();
        int px = camera.x;
        int py = camera.y;
        int pz = camera.z;

        String format = "%-" + (vw - 1) + "s";

        String playerCoordinates = "Pos: " + px + ", "+  py + ", " + pz;
        String pcf = String.format(format, playerCoordinates);
        writeText(tileGrid, pcf, 1, 1);

        String gameTurns = "turns: " +  gameState.getTurnCounter();
        writeText(tileGrid, gameTurns, 1, 3);

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
        writeText(tileGrid, text, x, y, tileGrid.getForegroundColor(), tileGrid.getBackgroundColor());
    }

    public void writeText(DrawSurface tileGrid, String text, int x, int y, TileColor fg, TileColor bg) {
        for (int cursor = 0; cursor < text.length(); cursor++) {
            tileGrid.setTileAt(Positions.create(x + cursor, y),
                    Tiles.newBuilder()
                            .withCharacter(text.charAt(cursor))
                            .withForegroundColor(fg)
                            .withBackgroundColor(bg)
                            .build());
        }
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
