package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.entity.EntityType;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.Bresenham;
import com.aaroncarsonart.tarotrl.util.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private static final Logger LOG = new Logger(GameMap3D.class);
    private static final int DEFAULT_Z_COORDINATE = 0;

    private Map<Position3D, MapVoxel> worldMap;
    private Position3D camera;

    private Map<Position3D, MapEntity> entities;
    private List<Region3D> levelRegions;

    public GameMap3D() {
        // estimate screen size in tiles as initial hashmap capacity
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthInTiles = screenSize.width / 16;
        int heightInTiles = screenSize.height / 16;
        int initialCapacity = widthInTiles * heightInTiles;
        worldMap = new HashMap<>(initialCapacity);
        entities = new LinkedHashMap<>();
        levelRegions = new ArrayList<>();
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

    /**
     * Visit every MapVoxel within the given region, applying the visitor
     * function to every MapVoxel.
     *
     * @param region The region to visit.
     * @param visitor The visitor function to apply to every visited MapVoxel.
     */
    public void visit(Region3D region, Consumer<MapVoxel> visitor) {
        Position3D start = region.position;
        Position3D max = start.add(region.dimensions);
        visit (start, max, visitor);
    }

    /**
     * Visit every MapVoxel within the given region defined by the start and
     * max positions. applying the visitor function to every MapVoxel.
     *
     * @param start The smallest position of the region to visit.
     * @param max The largest position of the region to visit.
     * @param visitor The visitor function to apply to every visited MapVoxel.
     */
    public void visit(Position3D start, Position3D max, Consumer<MapVoxel> visitor) {
        LOG.info("visit region: " + start + " to " + max);
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

    private TileDefinitionSet tileDefinitionSet;

    public TileDefinitionSet getTileDefinitionSet() {
        return tileDefinitionSet;
    }

    public void setTileDefinitionSet(TileDefinitionSet tileDefinitionSet) {
        this.tileDefinitionSet = tileDefinitionSet;
    }

    public MapEntity getFirstMapEntityMatching(Predicate<MapEntity> predicate) {
        return entities.values().stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the level region for the current depth (the z value).
     * @return the level region for the current depth.
     */
    public Region3D getActiveLevelRegion() {
        int depth = camera.z;
        return getLevelRegion(depth);
    }

    /**
     * Get the level region for the given input depth (the z value).
     * @param depth The z value for the level region to get.
     * @return the level region for the given input depth.
     */
    public Region3D getLevelRegion(int depth) {
        if (levelRegions.isEmpty()) {
            levelRegions.addAll(calculateLevelRegions());
        }
        int index = depth * -1;
        Region3D levelRegion = levelRegions.get(index);
        return levelRegion;
    }

    /**
     * Calculates all level regions for this GameMap3D. Each region is
     * a horizontal slice of the map, sharing the same depth.
     *
     * @return The list of all level regions, grouped by depth.
     */
    private List<Region3D> calculateLevelRegions() {
        List<Region3D> levelRegions = new ArrayList<>();
        Map<Integer, List<MapVoxel>> voxelsGroupedByDepth = worldMap.values().stream()
                .collect(Collectors.groupingBy(v -> v.position.z));

        for (Map.Entry<Integer, List<MapVoxel>> entry : voxelsGroupedByDepth.entrySet()) {
            List<MapVoxel> voxels = entry.getValue();

            Position3D start = voxels.get(0).position;
            int minX = start.x;
            int maxX = start.x;
            int minY = start.y;
            int maxY = start.y;
            int z = entry.getKey();

            for(MapVoxel voxel : voxels) {
                Position3D current = voxel.position;
                minX = Math.min(minX, current.x);
                maxX = Math.max(maxX, current.x);
                minY = Math.min(minY, current.y);
                maxY = Math.max(maxY, current.y);
            }

            Position3D min = new Position3D(minX, minY, z);
            Position3D max = new Position3D(maxX, maxY, z).withRelativeZ(1);

            // Grow region by one horizontally and vertically to allow for visibility
            // processing on map borders.
            // maxOffset x and y must be offset by an additional point to include the
            // outermost edges as regions are exclusive with regards to upper boundaries.
            Position3D minOffset = new Position3D(1, 1, 0);
            Position3D maxOffset = new Position3D(2, 2, 0);
            min = min.subtract(minOffset);
            max = max.add(maxOffset);

            Position3D position = min;
            Position3D dimensions = max.subtract(min);

            Region3D levelRegion = new Region3D(position, dimensions);
            levelRegions.add(levelRegion);
        }
        return levelRegions;
    }

    /**
     * Calculate the current field of view for the player.
     * @param state The GameState to use.
     */
    public void calculatePlayerFov(GameState state) {
        Region3D activeLevelRegion = getActiveLevelRegion();

        // clear old lighting
        visit(activeLevelRegion, voxel -> {
            if (voxel.getVisibility() == MapVoxel.VISIBLE) {
                voxel.setVisibility(MapVoxel.KNOWN);
            }
        });

        // update new lighting
        int fovRange = state.getPlayerFovRange();
        List<Position3D> visiblePositions = fov(camera, fovRange);
        for (Position3D current : visiblePositions) {
            MapVoxel voxel = getVoxel(current);
            voxel.setVisibility(MapVoxel.VISIBLE);
        }
    }

    /**
     * Get the list of visible positions for the field of view
     * defined by the given position and range.
     *
     * @param center The position to calculate the fov for.
     * @param range The range of the fov.
     * @return The list of visible positions for the given fov.
     */
    public List<Position3D> fov(Position3D center, int range) {
        List<Position3D> visiblePositions = new ArrayList<>();

        // use simple line tracing
        int z = center.z;
        for (int x = center.x - range; x < center.x + range; x++) {
            for (int y = center.y - range; y < center.y + range; y++) {
                Position3D current = new Position3D(x, y, z);
                if (center.distance(current) <= range) {
                    List<Position2D> line = Bresenham.plotLine(center.x, center.y, x, y);

                    // the first position is always visible
                    Iterator<Position2D> it = line.iterator();
                    Position3D next = it.next().to3D(z);
                    visiblePositions.add(next);

                    while (it.hasNext()) {
                        next = it.next().to3D(z);
                        MapVoxel voxel = getVoxel(next);
                        visiblePositions.add(next);

                        // check if a blocking character is encountered
                        char c = voxel.getTileType().getSprite();
                        if ("#+ ".indexOf(c) != -1) {
                            break;
                        }
                    }
                }
            }
        }
        return visiblePositions;
    }

}
