package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.map.TileType;

/**
 * Optional class for defining a cubic unit of a GameWorld.
 */
public class WorldBlock {
    public final Position3D origin;
    public final Position3D dimensions;
    public final WorldVoxel[][][] voxels;

    /**
     * Create a new WorldBlock with the given origin and DIMENSIONS.
     * @param position The position within the GameWorld.
     */
    public WorldBlock(GameWorld world, Position3D position, Position3D dimensions) {
        this.origin = position;
        this.dimensions = dimensions;
        this.voxels = Position3D.createGrid3D(position, dimensions, (x, y, z) -> {
            Position3D voxelPosition = new Position3D(x, y, z);
            TileType tileType = TileType.EMPTY;
            return new WorldVoxel(world, voxelPosition, tileType);
        });
    }

    public WorldVoxel getVoxel(int x, int y, int z) {
        return voxels[x][y][z];
    }

    public WorldVoxel getVoxel(Position3D position) {
        return getVoxel(position.x, position.y, position.z);
    }
}
