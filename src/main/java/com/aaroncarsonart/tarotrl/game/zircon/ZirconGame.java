package com.aaroncarsonart.tarotrl.game.zircon;

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

/**
 * The main class for encapsulating all game logic when using the Zircon UI.
 */
public class ZirconGame {

    public static GameState initGameState() {
        GameState gameState = new GameState();

        gameState.setPlayerPosY(1);
        gameState.setPlayerPosX(1);

//        Maze maze = Maze.generateRandomWalledMaze(21, 21);
//        GameMap gameMap = GameMapUtils.createGameMapFromMaze("Maze Map", maze);
        GameMap gameMap = GameMapUtils.readFileAsGameMap("/maps/test_game_map.txt");

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

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
                        .withDefaultTileset(CP437TilesetResources.mdCurses16x16())
                        .build());

        GameActionHandler actionHandler = new GameActionHandler();
        ZirconInputHandler inputHandler = new ZirconInputHandler();
        tileGrid.onKeyStroke(inputHandler::handleKeyStroke);

        ZirconTileRenderer tileRenderer = new ZirconTileRenderer();
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
