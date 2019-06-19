package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;

/**
 * Shared interface for 2D and 3D maps.
 */
public interface GameMap {

    boolean isPassable(int y, int x);
    boolean isPassable(Position2D position);
    boolean isPassable(Position3D position);

    TileType getTileType(int y, int x);
    TileType getTileType(Position2D position);
    TileType getTileType(Position3D position);

    MapVoxel getVoxel(int x, int y, int z);
    MapVoxel getVoxel(Position3D position);

    void addEntity(MapEntity entity);
    boolean hasEntity(Position3D position);
    boolean hasItem(Position3D position);
    MapEntity getEntity(Position3D position);
    MapEntity removeEntity(Position3D position);
    void moveEntity(MapEntity entity, Position3D current);

    void update();

    Position3D getCamera();
    void setCamera(Position3D camera);

    Position2D findFirstOccurrence2D(TileType tileType);
    Position3D findFirstOccurrence3D(TileType tileType);
}
