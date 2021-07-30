package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
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
     *
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

        int maxX = viewPort.x + viewPort.width - endOffset - 1;

        // draw top horizontal line
        for (int x = viewPort.x + startOffset; x < maxX; x++) {
            int y = viewPort.y;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_HORIZONTAL)
                    .build());
        }

        // draw bottom horizontal line
        for (int x = viewPort.x + startOffset; x < maxX; x++) {
            int y = viewPort.y + viewPort.height - 1;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_HORIZONTAL)
                    .build());
        }

        int maxY = viewPort.y + viewPort.height - endOffset - 1;

        // draw left vertical line
        for (int y = viewPort.y + startOffset; y < maxY; y++) {
            int x = viewPort.x;
            Position next = Positions.create(x, y);
            tileGrid.setTileAt(next, Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(fg)
                    .withCharacter(Symbols.SINGLE_LINE_VERTICAL)
                    .build());
        }

        // draw right vertical line
        for (int y = viewPort.y + startOffset; y < maxY; y++) {
            int x = viewPort.x + viewPort.width - 1;
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

        Position topRight = Positions.create(viewport.x + viewport.width - 1, viewport.y);
        tileGrid.setTileAt(topRight, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_TOP_RIGHT_CORNER)
                .build());

        Position bottomLeft = Positions.create(viewport.x, viewport.y + viewport.height - 1);
        tileGrid.setTileAt(bottomLeft, Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER)
//                .withCharacter(Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER)
                .build());

        Position bottomRight = Positions.create(viewport.x + viewport.width - 1, viewport.y + viewport.height - 1);
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
                .withForegroundColor(GameColors.BLUE_1)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile cups = Tiles.newBuilder()
                .withCharacter(Symbols.HEART)
                .withForegroundColor(GameColors.GREEN_1)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile wands = Tiles.newBuilder()
                .withCharacter(Symbols.CLUB)
                .withForegroundColor(GameColors.RED_1)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Tile pentacles = Tiles.newBuilder()
                .withCharacter(Symbols.DIAMOND)
                .withForegroundColor(GameColors.YELLOW_1)
                .withBackgroundColor(GameColors.BLACK)
                .build();

        Position topLeftCorner = Positions.create(viewport.x, viewport.y);
        Position topRightCorner = Positions.create(viewport.x + viewport.width - 1, viewport.y);
        Position bottomLeftCorner = Positions.create(viewport.x, viewport.y + viewport.height - 1);
        Position bottomRightCorner = Positions.create(viewport.x + viewport.width - 1, viewport.y + viewport.height - 1);

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

    /**
     * Write The given text within a box. The height is dynamically
     * calculated, based on the length of the text.
     *
     * @param tileGrid The TileGrid to draw the text and box on.
     * @param text     The text to write.
     * @param x        The top-left x coordinate of the box.
     * @param y        The top-left y coordinate of the box.
     * @param boxWidth The width of the box.
     */
    default void writeTextInBox(TileGrid tileGrid, String text, TextAlignment alignment,
                                int x, int y, int boxWidth) {
        if (boxWidth <= 2) {
            throw new IllegalArgumentException("boxWidth must be greater than 2 for text to display in the box.");
        }
        int messageWidth = boxWidth - 2;
        List<String> textList = TextUtils.getWordWrappedText(text, messageWidth);
        writeTextInBox(tileGrid, textList, alignment, x, y, boxWidth);
    }

    /**
     * Write The given text within a box. The height is dynamically
     * calculated, based on the length of the text.
     *
     * @param tileGrid The TileGrid to draw the text and box on.
     * @param textList The text to write.
     * @param x        The top-left x coordinate of the box.
     * @param y        The top-left y coordinate of the box.
     * @param boxWidth The width of the box.
     */
    default void writeTextInBox(TileGrid tileGrid, List<String> textList, TextAlignment alignment,
                                int x, int y, int boxWidth) {
        if (boxWidth <= 2) {
            throw new IllegalArgumentException("boxWidth must be greater than 2 for text to display in the box.");
        }
        int textWidth = boxWidth - 2;
        int textHeight = textList.size();
        int boxHeight = textHeight + 2;
        ViewPort boxViewPort = new ViewPort(x, y, boxWidth, boxHeight);
        drawSimpleBorder(tileGrid, boxViewPort, false);

        switch (alignment) {
            case LEFT: {
                for (int i = 0; i < textHeight; i++) {
                    String text = textList.get(i);
                    int cx = 1 + x;
                    int cy = 1 + y + i;
                    writeText(tileGrid, text, cx, cy);
                }
                break;
            }
            case RIGHT: {
                for (int i = 0; i < textHeight; i++) {
                    String text = textList.get(i);
                    int cx = 1 + x + textWidth - text.length();
                    int cy = 1 + y + i;
                    writeText(tileGrid, text, cx, cy);
                }
                break;
            }
            case CENTER: {
                int mx = textWidth / 2;
                for (int i = 0; i < textHeight; i++) {
                    String text = textList.get(i);
                    int cx = 1 + x + mx - text.length() / 2;
                    int cy = 1 + y + i;
                    writeText(tileGrid, text, cx, cy);
                }
                break;
            }
        }
    }
}
