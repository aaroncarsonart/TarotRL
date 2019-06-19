package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.map.TileType;

/**
 * Optional class for defining a cubic unit of a GameMap3D.
 */
public class WorldBlock {
    public final Position3D origin;
    public final Position3D dimensions;
    public final MapVoxel[][][] voxels;

    /**
     * Create a new WorldBlock with the given origin and DIMENSIONS.
     * @param position The position within the GameMap3D.
     */
    public WorldBlock(GameMap3D world, Position3D position, Position3D dimensions) {
        this.origin = position;
        this.dimensions = dimensions;
        this.voxels = Position3D.createGrid3D(position, dimensions, (x, y, z) -> {
            Position3D voxelPosition = new Position3D(x, y, z);
            TileType tileType = TileType.EMPTY;
            return new MapVoxel(world, voxelPosition, tileType);
        });
    }

    public MapVoxel getVoxel(int x, int y, int z) {
        return voxels[x][y][z];
    }

    public MapVoxel getVoxel(Position3D position) {
        return getVoxel(position.x, position.y, position.z);
    }
}
