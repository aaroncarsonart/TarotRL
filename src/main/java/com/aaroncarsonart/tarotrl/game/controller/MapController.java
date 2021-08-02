package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.MapActionModifier;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.inventory.Item;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.inventory.Treasure;
import com.aaroncarsonart.tarotrl.map.Direction2D;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;

import java.util.List;
import java.util.function.Predicate;

/**
 * This drives all game state behavior for the MAP_NAVIGATION GameMode.
 */
public class MapController implements GameController {
    private static final Logger LOG = new Logger(MapController.class);

    @Override
    public boolean update(GameState state, UserInput input) {
        state.setDevMode(input.hasModifer(MapActionModifier.DEV_MODE));
        state.setPlayerMoved(false);

        Enum action = input.getAction();
        if (action instanceof PlayerAction) {
            PlayerAction playerAction = (PlayerAction) action;
            processPlayerAction(playerAction, state);
        }

        if (state.playerMoved()) {
            GameMap3D activeMap = state.getActiveGameMap();
            activeMap.calculatePlayerFov(state);
        }

        return true;
    }

    /**
     * ConsoleGame method of class. Handle the nextInt PlayerAction, and update the game state.
     * @param nextAction
     */
    public void processPlayerAction(PlayerAction nextAction, GameState gameState) {
        gameState.setPreviousAction(gameState.getCurrentAction());
        gameState.setCurrentAction(nextAction);

        if (Logger.ENABLE_TRACE_LEVEL) {

            String format = "| %-50s |";

            LOG.trace("+----------------------------------------------------+");
            LOG.trace(format, String.format("          turn: %s", gameState.getTurnCounter()));
            LOG.trace(format, String.format("      position: %s", gameState.getActiveGameMap().getCamera()));
            LOG.trace(format, String.format("previousAction: %s", gameState.getPreviousAction()));
            LOG.trace(format, String.format(" currentAction: %s", gameState.getCurrentAction()));
            LOG.trace(format, String.format("       devMode: %s", gameState.isDevMode()));
            LOG.trace(format, String.format("     shiftDown: %s", gameState.isShiftDown()));
            LOG.trace("+----------------------------------------------------+");
        }
        switch (nextAction) {

            case QUIT:
                gameState.setGameOver(true);
                LOG.info("Quitting game.");
                break;

            case SNAPSHOT_HISTORY:
                gameState.setGameMode(GameMode.SNAPSHOT_HISTORY);
                gameState.setRepeatControllerUpdate(true);
                break;

            case HELP:
                doDisplayHelpScreen();
                break;

            case MOVE_UP:
                doMove(gameState, Direction2D.NORTH);
                break;

            case MOVE_DOWN:
                doMove(gameState, Direction2D.SOUTH);
                break;

            case MOVE_LEFT:
                doMove(gameState, Direction2D.WEST);
                break;

            case MOVE_RIGHT:
                doMove(gameState, Direction2D.EAST);
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

            case INVENTORY:
                gameState.setGameMode(GameMode.INVENTORY);
                break;

            case AUTO_PICKUP_ITEMS:
                doToggleAutoCollect(gameState);
                break;

            case DEV_COLLECT_TAROT_CARD:
                doCollectTarotCard(gameState);
                break;

            case MAP_LEVEL:
                doMapLevel(gameState);
                break;

            default:
                LOG.warning("Case not handled: PlayerAction." + nextAction.name());
        }
        gameState.incrementTurnCounter();
    }

    private void doToggleAutoCollect(GameState gameState) {
        gameState.toggleAutoCollect();
        boolean autoCollect = gameState.isAutoCollectMode();
        if (autoCollect) {
            gameState.setStatus("Auto-collect mode enabled. Automatically collect items " +
                    "and add them to your inventory when you cross the tile they ocuppy.");
        } else {
            gameState.setStatus("Auto-collect mode disabled. Items must be collected using " +
                    "CONFIRM to be added to your inventory.");
        }
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
        GameMap world = gameState.getActiveGameMap();

        // check if requested origin is available to be moved into.
        Position3D camera = world.getCamera();
        Position3D targetPosition = camera.moveTowards(direction);
        MapVoxel targetVoxel = world.getVoxel(targetPosition);
        TileType tileType = targetVoxel.getTileType();

        // SHIFT mode: context-sensitive CONFIRM.
        boolean shiftDown = gameState.isShiftDown();
        if (shiftDown) {
            if (tileType.isDoor()) {
                toggleDoor(gameState, targetVoxel);
            } else if (gameState.isAutoCollectMode()) {
                attemptCollectItem(gameState, targetPosition);
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

        // BUMP to open doors.
        else if (tileType == TileType.CLOSED_DOOR &&
                targetPosition.equals(gameState.getInspectedPosition())) {
            toggleDoor(gameState, targetVoxel);
        }

        // TODO: bump to attack monsters

        // MOVE mode: try to move into the nextInt position on the map.
        else {
            boolean moved = attemptMove(gameState, targetVoxel, direction);
            gameState.setPlayerMoved(moved);
        }
    }

    private void attemptCollectItem(GameState gameState, Position3D position) {
        GameMap world = gameState.getActiveGameMap();
        if (world.hasItem(position)) {
            ItemEntity itemEntity = (ItemEntity) world.removeEntity(position);
            Item item = itemEntity.getItem();

            if (item instanceof TarotCardItem) {
                gameState.addTarotCardToDeck((TarotCardItem) item);
                gameState.setStatus(TextUtils.capitalize(item.getName())
                        + " has been OBTAINED. ADDED to your deck.");
            } else if (item instanceof Treasure) {
                Treasure treasure = (Treasure) item;
                int amount = treasure.getAmount();
                gameState.gainTreasure(amount);
                gameState.setStatus("Gained " + amount + " coins of treasure!");
            } else {
                gameState.getEncounteredItems().put(item.getName(), item);
                gameState.getPlayerItems().add(item);
                gameState.setStatus(TextUtils.capitalize(item.getName())
                        + " has been added to your inventory.");
            }
        }
    }

    private boolean attemptMove(GameState gameState, MapVoxel voxel, Direction2D originDirection) {
        if (gameState.isDevMode() || voxel.isPassable()) {
            voxel.map.setCamera(voxel.position);

            LOG.trace("Move player: %s", originDirection.getDirection3D());

            String status = getMoveStatus(voxel.map, voxel.position);
            if (status != null) {
                gameState.setStatus(status);
            }
            gameState.incrementStepCount();

            if (gameState.isAutoCollectMode()) {
                attemptCollectItem(gameState, voxel.position);
            }
            return true;
        }

        // INSPECT mode; if can't move, just inspect the current space.
        else {
            gameState.setCurrentAction(PlayerAction.INSPECT);
            doInspect(gameState, originDirection);
            return false;
        }
    }


    private void toggleDoor(GameState gameState, MapVoxel voxel) {
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

    /**
     * Portals '%' warp from one GameMap to another GameMap.
     */
    private void attemptPortal(GameState gameState, MapVoxel voxel) {
        //TODO implement
    }

    private boolean attemptStairs(GameState gameState, MapVoxel voxel) {
        TileType tileType = voxel.getTileType();
        if (tileType == TileType.UPSTAIRS) {
            MapVoxel upstairs = voxel.getNeighbor(Direction3D.ABOVE);
            if (gameState.isDevMode() || upstairs.isPassable()) {
                voxel.map.setCamera(upstairs.position);
                gameState.incrementStepCount();
                gameState.setStatus("You ascended the stairs.");
                gameState.setCurrentAction(PlayerAction.ASCEND);
                return true;
            } else {
                // TODO reference above tile's material type, when implemented.
                gameState.setStatus("You tried to ascend the stairs, " +
                        "but the stairway was obstructed by immovable debris.");
            }
        } else if (tileType == TileType.DOWNSTAIRS) {
            MapVoxel downstairs = voxel.getNeighbor(Direction3D.BELOW);
            if (gameState.isDevMode() || downstairs.isPassable()) {
                voxel.map.setCamera(downstairs.position);
                gameState.incrementStepCount();
                gameState.setStatus("You descended the stairs.");
                gameState.setCurrentAction(PlayerAction.DESCEND);
                return true;
            } else {
                // TODO reference above tile's material type, when implemented.
                gameState.setStatus("You tried to descend the stairs, " +
                        "but the stairway was obstructed by immovable debris.");
            }
        }
        return false;
    }

    /**
     * Create a special status message for tiles that are worth mentioning
     * to the player in the StatusLog, simply for walking over them.
     * @param world The current GameMap.
     * @param position The origin being moved over.
     * @return The special status message, otherwise null.
     */
    public String getMoveStatus(GameMap world, Position3D position) {
        MapVoxel voxel = world.getVoxel(position);
        TileType tileType = voxel.getTileType();
        if (world.hasEntity(position)) {
            String inspectMessage = Direction2D.NONE.getInspectString() + " " + voxel.getDescription();
            inspectMessage = TextUtils.capitalize(inspectMessage);
            return inspectMessage;
        } else if (tileType == TileType.DOWNSTAIRS || tileType == TileType.UPSTAIRS) {
            String inspectMessage = Direction2D.NONE.getInspectString() + " " + voxel.getDescription();
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
        GameMap world = gameState.getActiveGameMap();
        Position3D playerPosition = world.getCamera();
        Position3D target = playerPosition.moveTowards(inspectDirection);
        gameState.setInspectedPosition(target);

        MapVoxel voxel = world.getVoxel(target);
        String inspectStatus = inspectDirection.getInspectString() + " " + voxel.getDescription();
        inspectStatus = TextUtils.capitalize(inspectStatus);

        gameState.setStatus(inspectStatus);
    }

    /**
     * The CONFIRM action is context-sensitive, so it will have many overloaded
     * behaviors based on the current state of the game and what the player is doing.
     * @param gameState The current GameState.
     */
    private void doConfirm(GameState gameState) {
        GameMap world = gameState.getActiveGameMap();
        Position3D current = world.getCamera();

        if (world.hasItem(current)) {
            attemptCollectItem(gameState, current);
            return;
        }

        MapVoxel voxel = world.getVoxel(current);
        TileType tileType = voxel.getTileType();

        // handle portal
        if (tileType.isPortal()) {
            attemptPortal(gameState, voxel);
            if (gameState.getCurrentAction() != PlayerAction.CONFIRM) {
                return;
            }
        }

        // handle stairs
        if (tileType.isStairs()) {
            boolean moved = attemptStairs(gameState, voxel);
            gameState.setPlayerMoved(moved);
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

        // If the previousIndex action inspected a door, and the
        // player is standing nextInt to that door, the nextInt CONFIRM
        // action will open that door.
        if (gameState.getPreviousAction() == PlayerAction.INSPECT) {
            Position3D inspectedPosition = gameState.getInspectedPosition();
            MapVoxel inspectedVoxel = world.getVoxel(inspectedPosition);
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

    /**
     * For dev testing only. Auto collects the TarotCardItem found in the activeGameMap,
     * and adds this card to the player's deck, if the card is still present in the map
     * uncollected.
     * @param gameState The GameState to use.
     */
    public void doCollectTarotCard(GameState gameState) {
        GameMap3D gameMap = gameState.getActiveGameMap();

        // fetch the TarotCardItem, and remove it from the GameMap3D
        Predicate<MapEntity> isTarotCard = e -> e instanceof ItemEntity && ((ItemEntity) e).getItem() instanceof TarotCardItem;
        ItemEntity cardEntity = (ItemEntity) gameMap.getFirstMapEntityMatching(isTarotCard);

        // add the TarotCardItem to the player's deck
        if (cardEntity != null) {
            attemptCollectItem(gameState, cardEntity.getPosition());
        } else {
            gameState.setStatus("The tarot card for this map has already been collected.");
        }
    }

    /**
     * Map the current level, setting each MapVoxel's visible field to MAPPED.
     * @param gameState The GameState to use.
     */
    public void doMapLevel(GameState gameState) {
        GameMap3D gameMap = gameState.getActiveGameMap();
        Region3D activeRegion = gameMap.getActiveLevelRegion();
        gameMap.visit(activeRegion, voxel -> {

            Position3D pos = voxel.position;
            List<Position3D> allSurroundingNeighbors = pos.getAllSurroundingNeighborsForDepth();

            // skip tiles the player couldn't normally see
            int surroundingWalls = 0;
            for (Position3D neighbor : allSurroundingNeighbors) {
                MapVoxel neighborVoxel = gameMap.getVoxel(neighbor);
                TileType neighborType = neighborVoxel.getTileType();
                if (neighborType == TileType.WALL || neighborType == TileType.EMPTY) {
                    surroundingWalls++;
                }
            }
            boolean skipMappingTile = surroundingWalls == 8;

            // set all UNKNOWN tiles to MAPPED
            if (!skipMappingTile && voxel.getVisibility() == MapVoxel.UNKNOWN) {
                voxel.setVisibility(MapVoxel.MAPPED);
            }
        });
    }

}
