package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.console.ConsoleInputHandler;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameTileDefinition;

/**
 * Handle All proper GameActions, which update the GameState in some way.
 */
public class GameActionHandler {

    /**
     * ConsoleGame method of class. Handle the next PlayerAction, and update the game state.
     * @param nextAction
     */
    public void processPlayerAction(PlayerAction nextAction, GameState gameState) {
        gameState.setPreviousAction(gameState.getCurrentAction());
        gameState.setCurrentAction(nextAction);

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

            case WAIT:
                doWait(gameState);
                break;

            case REST:
                doRest(gameState);
                break;

            case CONFIRM:
                doConfirm(gameState);
                break;

            case CANCEL:
                doCancel(gameState);
                break;

            case DOOR:
                gameState.setStatus("Door mode. Open or close a door with ←↑↓→ keys.");
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
     * <p>
     * This may incur a number of the following effects:
     * <ol>
     *     <li>The entity moves into the adjacent open space.</li>
     *     <li>The entity bumps into a wall, doing nothing.</li>
     *     <li>The entity bumps into a door, doing nothing.</li>
     * </ol>
     *
     * @param gameState The game state to update.
     * @param direction The direction to attempt to move.
     */
    private void doMove(GameState gameState, Direction direction) {
        // check if requested position is available to be moved into.
        int py = gameState.getPlayerPosition().y();
        int px = gameState.getPlayerPosition().x();
        int ny = py + direction.getDy();
        int nx = px + direction.getDx();
        Position2D newPosition = new Position2D(nx, ny);

        GameMap gameMap = gameState.getActiveMap();

        boolean withinBounds = gameMap.withinBounds(ny, nx);
        boolean canMove = withinBounds && gameMap.isPassable(ny, nx);

        PlayerAction previousAction = gameState.getPreviousAction();
        if (previousAction == PlayerAction.DOOR) {

            // toggle the door open/closed, if the target position is a door.
            char tile = gameMap.getTile(newPosition);
            if (tile == TileType.CLOSED_DOOR.getSprite()) {
                gameMap.setTile(newPosition, TileType.OPEN_DOOR);
                gameState.setStatus("You opened the door.");
            } else if (tile == TileType.OPEN_DOOR.getSprite()) {
                gameMap.setTile(newPosition, TileType.CLOSED_DOOR);
                gameState.setStatus("You closed the door.");
            } else {
                gameState.setStatus("There is no door in that direction.");
            }
            // As you have "confirmed" you are acting upon a door
            gameState.setCurrentAction(PlayerAction.CONFIRM);
            return;
        }

        if (canMove) {
            gameState.setPlayerPosition(newPosition);
            String status = getMoveStatus(gameState, gameMap, newPosition);
            gameState.setStatus(status);
        } else {
            gameState.setCurrentAction(PlayerAction.INSPECT);
            doInspect(gameState, direction);
        }
    }

    /**
     * Create a special status message for tiles that are worth mentioning
     * to the player in the StatusLog, simply for walking over them.
     * @param gameState The current GameState.
     * @param gameMap The current GameMap.
     * @param pos The position being moved over.
     * @return The special status message, otherwise null.
     */
    public String getMoveStatus(GameState gameState, GameMap gameMap, Position2D pos) {
        GameTileDefinition tile = gameMap.getTileDefinition(pos);
        if (tile.getTileType() == TileType.DOWNSTAIRS || tile.getTileType() == TileType.UPSTAIRS) {
            String inspectMessage = getTileInspectMessage(gameState, pos, Direction.NONE);
            return inspectMessage;
        }
        return null;
    }

    /**
     * TODO: add resting/waiting logic
     * @param gameState The current GameState
     */
    private void doWait(GameState gameState) {
    }

    /**
     * TODO: add resting/waiting logic
     * @param gameState The current GameState
     */
    private void doRest(GameState gameState) {
    }

    private void doInspect(GameState gameState, Direction inspectDirection) {
        Position2D playerPosition = gameState.getPlayerPosition();
        String inspectStatus = getTileInspectMessage(gameState, playerPosition, inspectDirection);
        gameState.setStatus(inspectStatus);
    }

    private String getTileInspectMessage(GameState gameState, Position2D pos, Direction inspectDirection ) {
        Position2D positionToInspect = pos.moveTowards(inspectDirection.getImbroglioDirection());
        gameState.setInspectedPosition(positionToInspect);

        GameMap gameMap = gameState.getActiveMap();
        GameTileDefinition tile = gameMap.getTileDefinition(positionToInspect);

        String inspectString = inspectDirection.getInspectString();
        inspectString = inspectString.substring(0, 1).toUpperCase() + inspectString.substring(1);

        String description = tile.getTileType().getDescription();
        return inspectString + " " + description + ".";
    }

    /**
     * The CONFIRM action is context-sensitive, so it will have many overloaded
     * behaviors based on the current state of the game and what the player is doing.
     * @param gameState The current GameState.
     */
    private void doConfirm(GameState gameState) {
        GameMap gameMap = gameState.getActiveMap();

        // handle inspect ground
        PlayerAction previousAction = gameState.getPreviousAction();
//        PlayerAction currentAction = gameState.getCurrentAction();
        switch (previousAction) {
            case UNKNOWN:
            case MOVE_DOWN:
            case MOVE_UP:
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case WAIT:
                gameState.setCurrentAction(PlayerAction.INSPECT);
                doInspect(gameState, Direction.NONE);
                return;
        }

        // If the previous action inspected a door, and the
        // player is standing next to that door, the next CONFIRM
        // action will open that door.
        if (previousAction == PlayerAction.INSPECT) {
            Position2D inspectedPosition = gameState.getInspectedPosition();
            GameTileDefinition inspectedTile = gameMap.getTileDefinition(inspectedPosition);
            if (inspectedTile.getTileType() == TileType.CLOSED_DOOR) {
                gameMap.setTile(inspectedPosition, TileType.OPEN_DOOR);
                gameState.setStatus("You opened the door.");
                gameState.setCurrentAction(PlayerAction.CONFIRM);
            }
        }
    }

    /**
     * The CANCEL action is context-sensitive, so it will have many overloaded
     * behaviors based on the current state of the game and what the player is doing.
     * @param gameState The current GameState.
     */
    private void doCancel(GameState gameState) {
    }
}
