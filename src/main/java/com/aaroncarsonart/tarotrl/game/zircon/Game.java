package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameMapUtils;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;

/**
 * The main class for encapsulating all game logic when using the Zircon UI.
 */
public class Game {

//    public static final TilesetResource REX_PAINT_8x8 = CP437TilesetResources.rexPaint8x8();
//    public static final TilesetResource MD_CURSES_16x16 = CP437TilesetResources.mdCurses16x16();
//    public static final TilesetResource TILESET = CP437TilesetResources.rexPaint8x8();
    public static final TilesetResource TILESET = CP437TilesetResources.rexPaint12x12();

    /**
     * Helper method to generate a byte[][] of paths and walls.
     *
     * @return a 2d byte array.
     */
    private static Maze generateCavernMap(int width, int height, int iterations) {
        Maze maze = Maze.generateCellularAutomataRoom(width, height);
        maze.connectDisconnectedComponents();
        for (int i = 0; i < iterations; i++) {
            maze.cellularAutomataIteration();
            maze.connectDisconnectedComponents();
        }
        return maze;
    }

    public static GameState initGameState() {
        GameState gameState = new GameState();

//        Maze maze = Maze.generateRandomWalledMaze(21, 21);
//        GameMap gameMap = GameMapUtils.createGameMapFromMaze("Maze Map", maze);

        Maze maze = generateCavernMap(500, 500, 2);
        GameMap gameMap = GameMapUtils.createGameMapFromMaze("Cave map", maze);

//        GameMap gameMap = GameMapUtils.readFileAsGameMap("/maps/test_game_map.txt");

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        Position2D start = maze.findFirstOccurrence(Maze.PATH);

        gameState.setPlayerPosY(start.y());
        gameState.setPlayerPosX(start.x());

        return gameState;
    }

    public static void runZirconGame() {
        //LinkedList<GameState> gameStates = new LinkedList<>();
        GameState gameState = initGameState();

        int windowWidth = 100;
        int windowHeight = 50;

        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(windowWidth, windowHeight))
                        .withDefaultTileset(TILESET)
                        .build());

        Position mapOffset = Positions.create(20, 2);
        Size mapDimensions = Sizes.create(windowWidth - 40, windowHeight - 12);
        ViewPort mapViewPort = new ViewPort(mapOffset, mapDimensions);

        Layer mapLayer1 = new LayerBuilder()
                .withOffset(mapOffset)
                .withSize(mapDimensions)
                .build();

        tileGrid.pushLayer(mapLayer1);

//        Tile magentaMod = Tiles.newBuilder()
//                .withForegroundColor(GameColors.MAGENTA)
//                .withBackgroundColor(TileColors.transparent())
//                .withCharacter('%')
//                .build();
//
//        Tile blueOverlay = Tiles.newBuilder()
//                .withBackgroundColor(TileColors.create(0, 0, 255, 50))
//                .withCharacter(' ')
//                .build();
//
//        mapLayer1.setTileAt(Positions.create(0, 0), magentaMod);
//
//        Layer mapLayer2 = new LayerBuilder()
//                .withOffset(mapOffset.plus(Positions.create(5, 5)))
//                .withSize(mapDimensions.minus(Sizes.create(10, 10)))
//                .build()
//                .fill(blueOverlay);
//
//        tileGrid.pushLayer(mapLayer2);

        GameActionHandler actionHandler = new GameActionHandler();
        InputHandler inputHandler = new InputHandler();
        tileGrid.onKeyStroke(inputHandler::handleKeyStroke);

        TileRenderer tileRenderer = new TileRenderer();
        tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
        tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);

        while (true) {
            PlayerAction nextAction = inputHandler.consumeNextAction();
            if (nextAction != PlayerAction.UNKNOWN) {
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
                tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        runZirconGame();
        // testScanner();
    }
}
