package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.map.Direction2D;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;

/**
 * Handle all interactions between Entities and movement on maps,
 * including collision detection and updating position info.
 */
public class MapEntityMovementController {
    private static final Logger LOG = new Logger(MapEntityMovementController.class);

    // TODO: Update PlayerActionController to use this code, somehow.
    // TODO: Probably by adding a Player GameEntity to GameState, etc.

    public boolean update(GameMap3D world, MapEntity entity, MapNavigationAction action, Direction2D direction) {
        switch (action) {
            case MOVE:   return attemptMove(world, entity, direction);
            case STAIRS: return attemptStairs(world, entity);
            case DOOR:   return attemptDoor(world, entity, direction);
            case TUNNEL: return attemptTunnel(world, entity, direction);
            case IDLE:
        }
        return false;
    }

    public boolean attemptMove(GameMap3D world, MapEntity entity, Direction2D direction) {
        if (direction == Direction2D.NONE) {
            LOG.warning("Attempted to move %s entity to Direction.NONE", entity, direction);
            return false;
        }
        Position3D target = entity.getPosition().moveTowards(direction);
        if (world.isPassable(target)) {
            world.moveEntity(entity, target);
            entity.incrementStepCount();
            entity.setStatus(" moved " + direction.getOrientationString() + ".");
            LOG.trace("Move %s entity %s to %s - successful", entity, direction, target);
            return true;
        } else {
            LOG.trace("Move %s entity %s to %s - path obstructed", entity, direction, target);
            return false;
        }
    }

    public boolean attemptStairs(GameMap3D world, MapEntity entity) {
        LOG.trace("MapEntity %s - attempt stairs", entity);
        Position3D target = entity.getPosition();
        MapVoxel voxel = world.getVoxel(target);
        TileType tileType = voxel.getTileType();

        boolean result = false;
        if (tileType == TileType.UPSTAIRS) {
            Position3D upstairs = target.moveTowards(Direction3D.ABOVE);
            if (entity.isIgnorePassable() || world.isPassable(upstairs)) {
                world.moveEntity(entity, upstairs);
                entity.incrementStepCount();
                entity.setStatus(" ascended the stairs.");
                result = true;
            }
            // TODO reference above tile's material type, when implemented.
            entity.setStatus(" tried to ascend the stairs, " +
                    "but the stairway was obstructed by immovable debris.");
        } else if (tileType == TileType.DOWNSTAIRS) {
            Position3D downstairs = target.moveTowards(Direction3D.BELOW);
            if (entity.isIgnorePassable() || world.isPassable(downstairs)) {
                world.moveEntity(entity, downstairs);
                entity.incrementStepCount();
                entity.setStatus(" descended the stairs.");
                result = true;
            } else {
                // TODO reference above tile's material type, when implemented.
                entity.setStatus(" tried to descend the stairs, " +
                        "but the stairway was obstructed by immovable debris.");
            }
        }
        return result;
    }

    private boolean attemptDoor(GameMap3D world, MapEntity entity, Direction2D direction) {
        LOG.trace("MapEntity %s - attempt door %s", entity, direction);
        Position3D target = entity.getPosition().moveTowards(direction);
        MapVoxel voxel = world.getVoxel(target);
        TileType tileType = voxel.getTileType();
        // toggle the door open/closed, if the target origin is a door.
        if (tileType == TileType.CLOSED_DOOR) {
            voxel.setTileType(TileType.OPEN_DOOR);
            entity.setStatus(" opened the door.");
            return true;
        } else if (tileType == TileType.OPEN_DOOR) {
            voxel.setTileType(TileType.CLOSED_DOOR);
            entity.setStatus(" closed the door.");
            return true;
        } else {
            entity.setStatus(" attempted to interact with a door, " +
                    "but there is no adjacent door in that direction.");
            return true;
        }
    }

    private boolean attemptTunnel(GameMap3D world, MapEntity entity, Direction2D direction) {
        LOG.trace("MapEntity %s - attempt tunnel %s", entity, direction);

        Position3D target = entity.getPosition().moveTowards(direction);
        MapVoxel voxel = world.getVoxel(target);
        TileType tileType = voxel.getTileType();
        if (tileType == TileType.WALL) {
            voxel.setTileType(TileType.PATH);
            entity.setStatus(" dug a tunnel to the " + direction.getOrientationString() + ".");
            return true;
        }
        return false;
    }
}
