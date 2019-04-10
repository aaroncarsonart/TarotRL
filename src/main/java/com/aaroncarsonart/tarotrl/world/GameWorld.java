package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.entity.Entity;
import com.aaroncarsonart.tarotrl.entity.EntityType;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    private Map<Position3D, Entity> entities;

    public GameWorld() {
        // estimate screen size in tiles as initial hashmap capacity
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthInTiles = screenSize.width / 16;
        int heightInTiles = screenSize.height / 16;
        int initialCapacity = widthInTiles * heightInTiles;
        worldMap = new HashMap<>(initialCapacity);
        entities = new LinkedHashMap<>();
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

    /**
     * @return The entire map of WorldVoxels in the GameWorld.
     *         Be cognizant of runtimes when using this map.
     */
    public Map<Position3D, WorldVoxel> getWorldMap() {
        return worldMap;
    }

    public boolean voxelRegionMatches(Region3D region, Predicate<WorldVoxel> predicate) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .allMatch(predicate);
    }

    public List<WorldVoxel> getMapRegion(Region3D region) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .collect(Collectors.toList());
    }

    public List<WorldVoxel> getMapRegionMatching(Region3D region, Predicate<WorldVoxel> predicate) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public void visit(Position3D start, Position3D max, Consumer<WorldVoxel> visitor) {
        for (int x = start.x; x < max.x; x++) {
            for (int y = start.y; y < max.y; y++) {
                for (int z = start.z; z < max.z; z++) {
                    WorldVoxel voxel = getVoxel(x, y, z);
                    visitor.accept(voxel);
                }
            }
        }
    }

    public void addEntity(Entity entity) {
        this.entities.put(entity.getPosition(), entity);
    }

    public boolean hasEntity(Position3D position) {
        return this.entities.containsKey(position);
    }

    public boolean hasItem(Position3D position) {
        return hasEntity(position) &&
               getEntity(position).getType() == EntityType.ITEM;
    }

    public Entity getEntity(Position3D position) {
        return this.entities.get(position);
    }

    public Entity removeEntity(Position3D position) {
        return this.entities.remove(position);
    }


    public void update() {
        for(Entity entity: entities.values()) {
            entity.update();
        }
    }
}
