package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(GameTileRenderer.class);

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        writeText(tileGrid, "Hello, map!", 0, 0);
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
