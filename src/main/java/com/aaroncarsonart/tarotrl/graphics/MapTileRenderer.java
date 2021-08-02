package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.List;

public class MapTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(MapTileRenderer.class);

    private ViewPort mapViewPort;

    public static ViewPort createMapViewPort(TileGrid tileGrid) {
        int windowWidth = tileGrid.getWidth();
        int windowHeight = tileGrid.getHeight();

        int xOffset = 1;
        int topOffSet = 1;
        int bottomOffSet = 10;
        Position vOffset = Positions.create(xOffset, topOffSet);

        int vWidth = windowWidth - (xOffset) - 1;
        int vHeight = windowHeight - (topOffSet + bottomOffSet);

        Size vDimensions = Sizes.create(vWidth, vHeight);
        return new ViewPort(vOffset, vDimensions);
    }

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        if (mapViewPort == null) {
            mapViewPort = createMapViewPort(tileGrid);
        }
        LOG.info("render(GameState gameState, TileGrid tileGrid)");
        renderTarotRLGame(tileGrid, gameState, mapViewPort);
    }

    private void renderTarotRLGame(TileGrid tileGrid, GameState gameState, ViewPort viewPort) {
        tileGrid.clear();
        renderGameMapThroughViewPort(tileGrid, gameState, viewPort, true);
        renderTarotRLStatusLog(tileGrid, gameState, viewPort);
        //logTarotRLTextStatus(tileGrid, gameState, viewPort);
    }

    private void logCurrentLevelRegion(GameMap3D world) {
        Region3D levelRegion = world.getLevelRegion(world.getCamera().z);
        Position3D levelOrigin = levelRegion.position;

        int levelWidth = levelRegion.dimensions.x;
        int levelHeight = levelRegion.dimensions.y;

        final char[][] mapData = new char[levelHeight][levelWidth];
        world.visit(levelRegion, voxel -> {
            Position3D pos = voxel.position.subtract(levelOrigin);
            mapData[pos.y][pos.x] = voxel.getTileType().getSprite();
        });

        int sbSize = levelWidth * levelHeight * 2;
        StringBuilder sb = new StringBuilder(sbSize);
        for (int y = 0; y < levelHeight; y++) {
            for (int x = 0; x < levelWidth; x++) {
                sb.append(mapData[y][x]);
                sb.append(' ');
            }
            sb.append('\n');
        }

        String levelStr = sb.toString();
        LOG.info("levelRegion map data:\n" + levelStr);
    }

    public void renderGameMapThroughViewPort(TileGrid tileGrid,
                                             GameState gameState,
                                             ViewPort viewPort,
                                             boolean drawViewportBorder) {
        GameMap3D world = gameState.getActiveGameMap();
//        logCurrentLevelRegion(world);

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

                char visibility = voxel.getVisibility();
                TileDefinition tileDefinition = tileType.getMetadata();
                tileDefinition = getTileDefinitionForVisibility(tileDefinition, visibility);

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

                    MapVoxel voxel = world.getVoxel(mapPos);
                    char visibility = voxel.getVisibility();
                    TileDefinition tileDefinition = entity.getTileDefinition();
                    tileDefinition = getTileDefinitionForVisibility(tileDefinition, visibility);

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

    /**
     * Get a TileDefinition for the given visibility value.
     * @param definition The TileDefinition to use, or get a modified copy, based on the visibility.
     * @param visibility The visibility value to get the TileDefinition for.
     * @return A TileDefinition for the given visibility value.
     */
    private TileDefinition getTileDefinitionForVisibility(TileDefinition definition, char visibility) {
        TileDefinition definitionForVisibility = null;

        // VISIBLE visibility tiles are drawn per the GameMap3D's color scheme.
        if (visibility == MapVoxel.VISIBLE)  {
            definitionForVisibility = definition;
        }

        // KNOWN visibility tiles are drawn in GREY.
        else if (visibility == MapVoxel.KNOWN) {
            boolean tileIsWall = definition.getTileType() == TileType.WALL;
            TileColor fgColor = definition.hasHighlight() ? GameColors.GREY_HIGHLIGHT : GameColors.GREY_2;
            TileColor bgColor = tileIsWall ? GameColors.BLACK : GameColors.GREY_4;
            definitionForVisibility = definition.copy();
            definitionForVisibility.setForegroundColor(fgColor);
            definitionForVisibility.setBackgroundColor(bgColor);
        }

        // UNKNOWN visibility tiles are drawn EMPTY in BLACK.
        else if (visibility == MapVoxel.UNKNOWN) {
            definitionForVisibility = definition.copy();
            definitionForVisibility.setTileType(TileType.EMPTY);
            definitionForVisibility.setSprite(TileType.EMPTY.getSprite());
            definitionForVisibility.setForegroundColor(GameColors.BLACK);
            definitionForVisibility.setBackgroundColor(GameColors.BLACK);
        }

        // MAPPED visibility tiles are drawn in GREEN.
        else if (visibility == MapVoxel.MAPPED) {
            boolean tileIsWall = definition.getTileType() == TileType.WALL;
            TileColor fgColor = definition.hasHighlight() ? GameColors.GREEN_HIGHLIGHT : GameColors.GREEN_2;
            TileColor bgColor = tileIsWall ? GameColors.BLACK : GameColors.GREEN_4;
            definitionForVisibility = definition.copy();
            definitionForVisibility.setForegroundColor(fgColor);
            definitionForVisibility.setBackgroundColor(bgColor);
        }

        return definitionForVisibility;
    }

    private void renderTarotRLStatusLog(TileGrid tileGrid,
                           GameState gameState,
                           ViewPort mapViewPort){
        // draw status log
        int lx = mapViewPort.x;
        int ly = mapViewPort.y + mapViewPort.height + 1;
        int lw = mapViewPort.width;
        int lh = tileGrid.getHeight() - (ly);

        ViewPort logViewPort = new ViewPort(lx, ly, lw, lh);
        writeSingleLogStatus(tileGrid, gameState, logViewPort);
        drawSimpleBorder(tileGrid, logViewPort, false);
    }


    private void logTarotRLTextStatus(TileGrid tileGrid,
                                        GameState gameState,
                                        ViewPort mapViewPort){
//        // draw player info
        int vw = 18;
//        int vh = 10;
//        ViewPort infoViewPort = new ViewPort(0, 0, vw, vh);
////        drawSimpleBorder(tileGrid, infoViewPort, false);
//
        Position3D camera = gameState.getActiveGameMap().getCamera();
        int px = camera.x;
        int py = camera.y;
        int pz = camera.z;

        String format = "%-" + (vw - 1) + "s";
        String playerCoordinates = "Pos: " + px + ", "+  py + ", " + pz;
        String pcf = String.format(format, playerCoordinates);
//        writeText(tileGrid, pcf, 1, 1);
        LOG.info(pcf);

        String gameTurns = "turns: " +  gameState.getTurnCounter();
//        writeText(tileGrid, gameTurns, 1, 3);
        LOG.info(gameTurns);

//        writeText(tileGrid, "last action:", 1, 5);
        String previousAction = String.format(format, gameState.getPreviousAction().name());
//        writeText(tileGrid, previousAction , 1, 6);
        LOG.info("last action: " + previousAction);

//        writeText(tileGrid, "current action:", 1, 8);
        String currentAction = String.format(format, gameState.getCurrentAction().name());
//        writeText(tileGrid, currentAction , 1, 9);
        LOG.info("current action: " + currentAction);

        String devMode = String.format(format, "devMode: " + gameState.isDevMode());
//        writeText(tileGrid, devMode , 1, 11);
        LOG.info(devMode);

        String shiftDown = String.format(format, "shiftDown: " + gameState.isShiftDown());
//        writeText(tileGrid, shiftDown , 1, 13);
        LOG.info(shiftDown);

//        writeText(tileGrid, "GameMode:", 1, 15);
        String gameMode = String.format(format, gameState.getActiveGameMode().name());
//        writeText(tileGrid, gameMode , 1, 16);
        LOG.info(gameMode);
    }

    private void writeSingleLogStatus(TileGrid tileGrid,
                                      GameState gameState,
                                      ViewPort logViewPort){
        int availableLogRows = logViewPort.height - 1;

        int sx = logViewPort.x + 1;
        int sy = logViewPort.y + 1;

        // clear the old status.
        int maxWidth = logViewPort.width - 2;

        String status = gameState.getStatus();
        if (StringUtils.isNotBlank(status)) {
            List<String> wrappedStatus = TextUtils.getWordWrappedText(status, maxWidth);

            for (int i = 0; i < Math.min(availableLogRows, wrappedStatus.size()); i++) {
                String nextStatus = wrappedStatus.get(i);
                writeText(tileGrid, nextStatus, sx, sy + i);
            }
        }
    }
}
