package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.entity.EntityType;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;

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
public class GameMap3D implements GameMap {
    private static final int DEFAULT_Z_COORDINATE = 0;

    /**
     * This WorldBlock contains the WorldVoxels from coordinates
     * (0, 0, 0) to (16, 16, 16).
     */
    private Map<Position3D, MapVoxel> worldMap;
    private Position3D camera;

    private Map<Position3D, MapEntity> entities;

    public GameMap3D() {
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

    @Override
    public Position2D findFirstOccurrence2D(TileType tileType) {
        return findFirstOccurrence3D(tileType).to2D();
    }

    @Override
    public Position3D findFirstOccurrence3D(TileType tileType) {
        for(MapVoxel voxel : getWorldMap().values()) {
            if (voxel.getTileType() == tileType) {
                return voxel.position;
            }
        }
        return null;
    }

    public MapVoxel getVoxel(int x, int y, int z) {
        return getVoxel(new Position3D(x, y, z));
    }

    public MapVoxel getVoxel(Position3D position) {
        if (worldMap.containsKey(position)) {
            return worldMap.get(position);
        }
        MapVoxel voxel = new MapVoxel(this, position);
        worldMap.put(position, voxel);
        return voxel;
    }

    @Override
    public boolean isPassable(int y, int x) {
        return isPassable(new Position3D(x, y, DEFAULT_Z_COORDINATE));
    }

    @Override
    public boolean isPassable(Position2D position) {
        return isPassable(position.to3D(DEFAULT_Z_COORDINATE));
    }

    public boolean isPassable(Position3D position) {
        MapVoxel voxel = getVoxel(position);
        return voxel.isPassable();
    }

    @Override
    public TileType getTileType(int y, int x) {
        return getTileType(new Position3D(x, y, DEFAULT_Z_COORDINATE));
    }

    @Override
    public TileType getTileType(Position2D position) {
        return getTileType(position.to3D(DEFAULT_Z_COORDINATE));
    }

    @Override
    public TileType getTileType(Position3D position) {
        return getVoxel(position).getTileType();
    }

    /**
     * @return The entire map of WorldVoxels in the GameMap3D.
     *         Be cognizant of runtimes when using this map.
     */
    public Map<Position3D, MapVoxel> getWorldMap() {
        return worldMap;
    }

    public boolean voxelRegionMatches(Region3D region, Predicate<MapVoxel> predicate) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .allMatch(predicate);
    }

    public List<MapVoxel> getMapRegion(Region3D region) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .collect(Collectors.toList());
    }

    public List<MapVoxel> getMapRegionMatching(Region3D region, Predicate<MapVoxel> predicate) {
        return Position3D.range(region).stream()
                .map(this::getVoxel)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public void visit(Position3D start, Position3D max, Consumer<MapVoxel> visitor) {
        for (int x = start.x; x < max.x; x++) {
            for (int y = start.y; y < max.y; y++) {
                for (int z = start.z; z < max.z; z++) {
                    MapVoxel voxel = getVoxel(x, y, z);
                    visitor.accept(voxel);
                }
            }
        }
    }

    /**
     * Move the MapEntity to occupy the new position.
     *
     * @param entity The MapEntity to update.
     * @param current The new position for the entity to be stored at.
     */
    public void moveEntity(MapEntity entity, Position3D current) {
        Position3D previous = entity.getPosition();
        entities.remove(previous);
        entities.put(current, entity);
        entity.setPosition(current);
    }

    public void addEntity(MapEntity entity) {
        this.entities.put(entity.getPosition(), entity);
    }

    public boolean hasEntity(Position3D position) {
        return this.entities.containsKey(position);
    }

    public boolean hasItem(Position3D position) {
        return hasEntity(position) &&
               getEntity(position).getType() == EntityType.ITEM;
    }

    public MapEntity getEntity(Position3D position) {
        return this.entities.get(position);
    }

    public MapEntity removeEntity(Position3D position) {
        return this.entities.remove(position);
    }


    public void update() {
        for(MapEntity entity: entities.values()) {
            entity.update();
        }
    }


    public Region3D calculateBoundingRegion3D() {
        Position3D min = Position3D.ORIGIN;
        Position3D max = Position3D.ORIGIN;

        for(MapVoxel voxel : worldMap.values()) {
            Position3D current = voxel.position;
            if (current.x < min.x) min = min.withX(current.x);
            if (current.y < min.y) min = min.withY(current.y);
            if (current.z < min.z) min = min.withZ(current.z);
            if (current.x > max.x) max = max.withX(current.x);
            if (current.y > max.y) max = max.withY(current.y);
            if (current.z > max.z) max = max.withZ(current.z);
        }

        Position3D position = min;
        Position3D dimensions = max.subtract(min);
        Region3D region = new Region3D(position, dimensions);
        return region;
    }

}
