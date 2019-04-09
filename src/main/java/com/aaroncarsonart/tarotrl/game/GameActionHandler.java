package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.WorldVoxel;

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
                doMove(gameState, Direction2D.UP);
                break;

            case MOVE_DOWN:
                doMove(gameState, Direction2D.DOWN);
                break;

            case MOVE_LEFT:
                doMove(gameState, Direction2D.LEFT);
                break;

            case MOVE_RIGHT:
                doMove(gameState, Direction2D.RIGHT);
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
                gameState.setStatus("Door mode. Open or close an adjacent door with ←↑↓→ keys.");
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
        // TODO implement
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
    private void doMove(GameState gameState, Direction2D direction) {
        GameWorld world = gameState.getGameWorld();

        // check if requested origin is available to be moved into.
        Position3D camera = world.getCamera();
        Position3D targetPosition = camera.moveTowards(direction);
        WorldVoxel targetVoxel = world.getVoxel(targetPosition);
        TileType tileType = targetVoxel.getTileType();

        // SHIFT mode: context-sensitive CONFIRM.
        if (gameState.isShiftDown()) {
            if (tileType.isDoor()) {
                toggleDoor(gameState, targetVoxel);
            } else {
                doInspect(gameState, direction);
            }
//            else if (tileType.isStairs())
//                attemptStairs(gameState, targetVoxel);
//            else {
//                attemptMove(gameState, targetVoxel);
//            }
        }

        // DOOR mode: open or close a door.
        else if (gameState.getPreviousAction() == PlayerAction.DOOR) {
            toggleDoor(gameState, targetVoxel);

            // As you have "confirmed" you are acting upon a door
            gameState.setCurrentAction(PlayerAction.CONFIRM);
        }

        // BUMP to open a doors.
        else if (tileType == TileType.CLOSED_DOOR &&
                targetPosition.equals(gameState.getInspectedPosition())) {
            toggleDoor(gameState, targetVoxel);
        }

        // MOVE mode: try to move into the next position on the map.
        else {
            attemptMove(gameState, targetVoxel, direction);
        }
    }

    private void attemptMove(GameState gameState, WorldVoxel voxel, Direction2D originDirection) {
        if (gameState.isDevMode() || voxel.isPassable()) {
            voxel.world.setCamera(voxel.position);
            String status = getMoveStatus(voxel.world, voxel.position);
            gameState.setStatus(status);
        }

        // INSPECT mode; if can't move, just inspect the current space.
        else {
            gameState.setCurrentAction(PlayerAction.INSPECT);
            doInspect(gameState, originDirection);
        }
    }


    private void toggleDoor(GameState gameState, WorldVoxel voxel) {
        TileType tileType = voxel.getTileType();
        // toggle the door open/closed, if the target origin is a door.
        if (tileType == TileType.CLOSED_DOOR) {
            voxel.setTileType(TileType.OPEN_DOOR);
            gameState.setStatus("You opened the door.");
        } else if (tileType == TileType.OPEN_DOOR) {
            voxel.setTileType(TileType.CLOSED_DOOR);
            gameState.setStatus("You closed the door.");
        } else {
            gameState.setStatus("There is no adjacent door in that direction.");
        }
    }

    private void attemptStairs(GameState gameState, WorldVoxel voxel) {
        TileType tileType = voxel.getTileType();
        if (tileType == TileType.UPSTAIRS) {
            WorldVoxel upstairs = voxel.getNeighbor(Direction3D.ABOVE);
            if (gameState.isDevMode() || upstairs.isPassable()) {
                voxel.world.setCamera(upstairs.position);
                gameState.setStatus("You ascended the stairs.");
                gameState.setCurrentAction(PlayerAction.ASCEND);
            } else {
                // TODO reference above tile's material type, when implemented.
                gameState.setStatus("You tried to ascend the stairs, " +
                        "but the stairway was obstructed by immovable debris.");
            }
        } else if (tileType == TileType.DOWNSTAIRS) {
            WorldVoxel downstairs = voxel.getNeighbor(Direction3D.BELOW);
            if (gameState.isDevMode() || downstairs.isPassable()) {
                voxel.world.setCamera(downstairs.position);
                gameState.setStatus("You descended the stairs.");
                gameState.setCurrentAction(PlayerAction.DESCEND);
            } else {
                // TODO reference above tile's material type, when implemented.
                gameState.setStatus("You tried to descend the stairs, " +
                        "but the stairway was obstructed by immovable debris.");
            }
        }
    }

    /**
     * Create a special status message for tiles that are worth mentioning
     * to the player in the StatusLog, simply for walking over them.
     * @param world The current GameWorld.
     * @param position The origin being moved over.
     * @return The special status message, otherwise null.
     */
    public String getMoveStatus(GameWorld world, Position3D position) {
        WorldVoxel voxel = world.getVoxel(position);
        TileType tileType = voxel.getTileType();
        if (tileType == TileType.DOWNSTAIRS || tileType == TileType.UPSTAIRS) {
            String inspectMessage = Direction2D.NONE.getInspectString() + " " + tileType.getDescription() + ".";
            inspectMessage = TextUtils.capitalize(inspectMessage);
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

    private void doInspect(GameState gameState, Direction2D inspectDirection) {
        GameWorld world = gameState.getGameWorld();
        Position3D playerPosition = world.getCamera();
        Position3D target = playerPosition.moveTowards(inspectDirection);
        gameState.setInspectedPosition(target);

        WorldVoxel voxel = world.getVoxel(target);
        TileType tileType = voxel.getTileType();
        String inspectStatus = inspectDirection.getInspectString() + " " + tileType.getDescription() + ".";
        inspectStatus = TextUtils.capitalize(inspectStatus);

        gameState.setStatus(inspectStatus);
    }

    /**
     * The CONFIRM action is context-sensitive, so it will have many overloaded
     * behaviors based on the current state of the game and what the player is doing.
     * @param gameState The current GameState.
     */
    private void doConfirm(GameState gameState) {
        GameWorld world = gameState.getGameWorld();
        Position3D current = world.getCamera();
        WorldVoxel voxel = world.getVoxel(current);
        TileType tileType = voxel.getTileType();

        // handle stairs
        if (tileType.isStairs()) {
            attemptStairs(gameState, voxel);
            if (gameState.getCurrentAction() != PlayerAction.CONFIRM) {
                return;
            }
        }

        // handle inspect ground
        switch (gameState.getPreviousAction()) {
            case UNKNOWN:
            case MOVE_DOWN:
            case MOVE_UP:
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case WAIT:
                gameState.setCurrentAction(PlayerAction.INSPECT);
                doInspect(gameState, Direction2D.NONE);
                return;
        }


        // If the previous action inspected a door, and the
        // player is standing next to that door, the next CONFIRM
        // action will open that door.
        if (gameState.getPreviousAction() == PlayerAction.INSPECT) {
            Position3D inspectedPosition = gameState.getInspectedPosition();
            WorldVoxel inspectedVoxel = world.getVoxel(inspectedPosition);
            TileType inspectedTileType = inspectedVoxel.getTileType();
            if (inspectedTileType == TileType.CLOSED_DOOR) {
                inspectedVoxel.setTileType(TileType.OPEN_DOOR);
                gameState.setStatus("You opened the door.");
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
