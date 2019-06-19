package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
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

public interface TileRenderer {
    Logger LOG = new Logger(TileRenderer.class);

    // https://www.fileformat.info/info/unicode/block/geometric_shapes/list.htm
    // ◢◣◤◥■▬	▮▰▲▶▼◀◀►◆◈◉

    void render(GameState gameState, TileGrid tileGrid);

    /**
     * Clear any previously set tile within the TileGrid.
     * @param tileGrid The TileGrid to clear.
     */
    default void clearTileData(TileGrid tileGrid) {
        for (Position position : tileGrid.createSnapshot().fetchPositions()) {
            tileGrid.setTileAt(position, Tiles.empty());
        }
    }


    default void writeText(TileGrid tileGrid, String text, int x, int y) {
        writeText(tileGrid, text, x, y, tileGrid.getForegroundColor(), tileGrid.getBackgroundColor());
    }

    default void writeText(DrawSurface tileGrid, String text, int x, int y, TileColor fg, TileColor bg) {
        for (int cursor = 0; cursor < text.length(); cursor++) {
            tileGrid.setTileAt(Positions.create(x + cursor, y),
                    Tiles.newBuilder()
                            .withCharacter(text.charAt(cursor))
                            .withForegroundColor(fg)
                            .withBackgroundColor(bg)
                            .build());
        }
    }

    default void drawBox(DrawSurface layer, Tile tile, int x, int y, int width, int height) {
        for (int mx = x; mx < width; mx++) {
            for (int my = y; my < height; my++) {
                layer.setTileAt(Positions.create(mx, my), tile);
            }
        }
    }

    default void drawSimpleBorder(TileGrid tileGrid, ViewPort viewPort, boolean decorateCorners) {
        if (decorateCorners) {
            drawDecoratedCorners(tileGrid, viewPort);
            drawBorderLines(tileGrid, viewPort, 1);
        } else {
            drawSimpleCorners(tileGrid, viewPort);
            drawBorderLines(tileGrid, viewPort, 0);
        }
    }

    default void drawBorderLines(TileGrid tileGrid, ViewPort viewPort, int cornerOffset) {
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

    default void drawSimpleCorners(TileGrid tileGrid, ViewPort viewport) {
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

    default void drawDecoratedCorners(TileGrid tileGrid, ViewPort viewport) {
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

    default void checkCoordinatesFit(TileGrid tileGrid, ViewPort viewPort) {
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

    default Tile createZirconTileFrom(TileDefinition definition) {
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
