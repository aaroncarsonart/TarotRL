package com.aaroncarsonart.tarotrl.world;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This holds all game coordinates that have been accessed by the
 * game at least once.  The map data is stored in WorldVoxels,
 * which are dynamically loaded as they are first accessed.
 */
public class GameWorld {

    /**
     * This WorldBlock contains the WorldVoxels from coordinates
     * (0, 0, 0) to (16, 16, 16).
     */
    private Map<Position3D, WorldVoxel> worldMap;
    private Position3D camera;

    public GameWorld() {
        // estimate screen size in tiles as initial hashmap capacity
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthInTiles = screenSize.width / 16;
        int heightInTiles = screenSize.height / 16;
        int initialCapacity = widthInTiles * heightInTiles;
        worldMap = new HashMap<>(initialCapacity);
    }

    public Position3D getCamera() {
        return camera;
    }

    public void setCamera(Position3D camera) {
        this.camera = camera;
    }

    public WorldVoxel getVoxel(int x, int y, int z) {
        return getVoxel(new Position3D(x, y, z));
    }

    public WorldVoxel getVoxel(Position3D position) {
        if (worldMap.containsKey(position)) {
            return worldMap.get(position);
        }
        WorldVoxel voxel = new WorldVoxel(this, position);
        worldMap.put(position, voxel);
        return voxel;
    }

    public boolean isPassable(Position3D position) {
        WorldVoxel voxel = getVoxel(position);
        return voxel.isPassable();
    }

    public void visit(Position3D origin, Position3D max, Consumer<WorldVoxel> visitor) {
        Position3D.forEach(origin, max, position3D -> {
            WorldVoxel voxel = getVoxel(position3D);
            visitor.accept(voxel);
        });
    }
}
