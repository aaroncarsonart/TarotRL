package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.imbroglio.Position;
import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameMapUtils;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;

/**
 * The main class for encapsulating all game logic when using the Zircon UI.
 */
public class Game {

//    public static final TilesetResource REX_PAINT_8x8 = CP437TilesetResources.rexPaint8x8();
//    public static final TilesetResource MD_CURSES_16x16 = CP437TilesetResources.mdCurses16x16();
//    public static final TilesetResource TILESET = CP437TilesetResources.rexPaint8x8();
    public static final TilesetResource TILESET = CP437TilesetResources.rexPaint10x10();

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

        Maze maze = generateCavernMap(50, 50, 2);
        GameMap gameMap = GameMapUtils.createGameMapFromMaze("Cave map", maze);

//        GameMap gameMap = GameMapUtils.readFileAsGameMap("/maps/test_game_map.txt");

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        Position start = maze.findFirstOccurrence(Maze.PATH);

        gameState.setPlayerPosY(start.y());
        gameState.setPlayerPosX(start.x());

        return gameState;
    }

    public static void runZirconGame() {
        //LinkedList<GameState> gameStates = new LinkedList<>();
        GameState gameState = initGameState();

        GameMap activeGameMap = gameState.getActiveMap();
        int width = activeGameMap.getWidth();
        int height = activeGameMap.getHeight();

        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(width, height))
                        .withDefaultTileset(TILESET)
                        .build());

        GameActionHandler actionHandler = new GameActionHandler();
        InputHandler inputHandler = new InputHandler();
        tileGrid.onKeyStroke(inputHandler::handleKeyStroke);

        TileRenderer tileRenderer = new TileRenderer();
        tileRenderer.renderGameState(tileGrid, gameState);

        while (true) {
            PlayerAction nextAction = inputHandler.consumeNextAction();
            if (nextAction != PlayerAction.UNKNOWN) {
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderGameState(tileGrid, gameState);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        runZirconGame();
        // testScanner();
    }
}
