package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A MapVoxel is a single unit of three-dimensional space
 * of a GameMap3D, uniquely identifiable by its coordinates.
 *
 * WorldVoxels hold map terrain data.
 */
public class MapVoxel {

    public final Position3D position;
    public final GameMap map;
    private TileType tileType;

    public MapVoxel(GameMap map, Position3D position, TileType tileType) {
        this.map = map;
        this.position = position;
        this.tileType = tileType;
    }

    public MapVoxel(GameMap world, Position3D position) {
        this(world, position, TileType.EMPTY);
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public MapVoxel getNeighbor(Direction3D direction) {
        Position3D neighboringPosition = position.moveTowards(direction);
        return map.getVoxel(neighboringPosition);
    }

    public List<MapVoxel> getNeighbors() {
        return Arrays.stream(Direction3D.values())
                .map(position::moveTowards)
                .map(map::getVoxel)
                .collect(Collectors.toList());
    }

    /**
     * Check if an entity can occupy this MapVoxel.
     * @return
     */
    public boolean isPassable() {
        return tileType.getMetadata().isPassable();
    }

    public boolean isUndefined() {
        return tileType == TileType.EMPTY;
    }

    public boolean hasEntity() {
        return map.hasEntity(position);
    }

    public boolean hasItem() {
        return map.hasItem(position);
    }

    public String getDescription() {
        String tileDescription = tileType.getDescription() + ".";
        if (hasEntity()) {
            MapEntity entity = map.getEntity(position);
            String entityDescription = entity.getDescription() + ".";

            // for some tiles, only show the entity description.
            if (tileType == TileType.EMPTY || tileType == TileType.PATH) {
                return entityDescription;
            }
            return entityDescription + " " + tileDescription;
        }
        return tileDescription;
    }
}
