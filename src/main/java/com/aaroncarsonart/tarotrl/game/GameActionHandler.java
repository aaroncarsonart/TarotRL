package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.game.console.ConsoleInputHandler;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;

/**
 * Handle All proper GameActions, which update the GameState in some way.
 */
public class GameActionHandler {

    /**
     * ConsoleGame method of class. Handle the next PlayerAction, and update the game state.
     * @param nextAction
     */
    public void processPlayerAction(PlayerAction nextAction, GameState gameState) {
         switch (nextAction) {

            case QUIT:
                doQuitGame();
                break;

            case HELP:
                doDisplayHelpScreen();
                break;

            case MOVE_UP:
                doMove(gameState, Direction.UP);
                break;

            case MOVE_DOWN:
                doMove(gameState, Direction.DOWN);
                break;

            case MOVE_LEFT:
                doMove(gameState, Direction.LEFT);
                break;

            case MOVE_RIGHT:
                doMove(gameState, Direction.RIGHT);
                break;

            default:
                System.out.println("Handle PlayerAction." + nextAction.name());
        }
        gameState.incrementTurnCounter();
    }

    /**
     * Do execute the "QUIT" game action.
     */
    private void doQuitGame() {
        System.out.println("Quitting game. Goodbye, thanks for playing!!! :-)");
        System.exit(0);
    }

    /**
     * Do execute the "HELP" game action.
     */
    private void doDisplayHelpScreen() {
        String consoleHelpString = ConsoleInputHandler.getConsoleHelpString();
        System.out.println(consoleHelpString);
    }

    /**
     * Do Execute the MOVE_* game actions.
     * @param gameState The game state to update.
     * @param direction The direction to attempt to move.
     */
    private void doMove(GameState gameState, Direction direction) {
        System.out.print("Attempt to move player " + direction.name().toLowerCase() + " ");

        // check if requested position is available to be moved into.
        int py = gameState.getPlayerPosY();
        int px = gameState.getPlayerPosX();
        int ny = py + direction.getDy();
        int nx = px + direction.getDx();

        System.out.printf("from (%d, %d) to (%d, %d): ", py, px, ny, nx);

        GameMap gameMap = gameState.getActiveMap();

        boolean canMove =  gameMap.withinBounds(ny, nx) && gameMap.isPassable(ny, nx);

        if (canMove) {
            // TODO implement move logic
            // TODO update map py/px

            gameState.setPlayerPosY(ny);
            gameState.setPlayerPosX(nx);

            System.out.println("Successful.");
        } else {
            System.out.println("Path was obstructed.");
        }
    }
}
