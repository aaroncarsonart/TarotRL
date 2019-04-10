package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.entity.Entity;
import com.aaroncarsonart.tarotrl.map.TileType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A WorldVoxel is a single unit of space of a GameWorld.
 * WorldVoxels exist within a 3 dimensional array within
 * a WorldBlock, which themselves exists within a
 * three-dimensional linked grid of WorldBlocks.
 */
public class WorldVoxel {

    public final GameWorld world;
    public final Position3D position;
    private TileType tileType;

    public WorldVoxel(GameWorld world, Position3D position, TileType tileType) {
        this.world = world;
        this.position = position;
        this.tileType = tileType;
    }

    public WorldVoxel(GameWorld world, Position3D position) {
        this(world, position, TileType.EMPTY);
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public WorldVoxel getNeighbor(Direction3D direction) {
        Position3D neighboringPosition = position.moveTowards(direction);
        return world.getVoxel(neighboringPosition);
    }

    public List<WorldVoxel> getNeighbors() {
        return Arrays.stream(Direction3D.values())
                .map(position::moveTowards)
                .map(world::getVoxel)
                .collect(Collectors.toList());
    }

    /**
     * Check if an entity can occupy this WorldVoxel.
     * @return
     */
    public boolean isPassable() {
        return tileType.getMetadata().isPassable();
    }

    public boolean isUndefined() {
        return tileType == TileType.EMPTY;
    }

    public boolean hasEntity() {
        return world.hasEntity(position);
    }

    public boolean hasItem() {
        return world.hasItem(position);
    }

    public String getDescription() {
        String tileDescription = tileType.getDescription() + ".";
        if (hasEntity()) {
            Entity entity = world.getEntity(position);
            String entityDescription = entity.getDescription() + ".";

            // for some tiles, only show the entity description.
            if (tileType == TileType.EMPTY || tileType == TileType.PATH) {
                return entityDescription;
            }
            return entityDescription + " " + tileType.getDescription();
        }
        return tileDescription;
    }
}