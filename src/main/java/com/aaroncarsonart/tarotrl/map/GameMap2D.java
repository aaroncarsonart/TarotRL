package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.deck.Element;
import com.aaroncarsonart.tarotrl.entity.EntityType;
import com.aaroncarsonart.tarotrl.entity.MapEntity;
import com.aaroncarsonart.tarotrl.graphics.GameColorSet;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * GameMaps are one element available for use during GameMap3D generation.
 * They represent a finite rectangular grid of tiles to be placed within
 * a GameMap3D along the x/y plane.
 *
 * This coordinate plane always starts with (0,0) as the top-left corner,
 * with (mapHeight, mapWidth) as the bottom right corner of the tileGrid.
 */
public class GameMap2D implements GameMap, Serializable {
    private static final Logger LOG = new Logger(GameMap2D.class).withLogLevel(LogLevel.INFO);

    private String name;
    private char[][] tileGrid;

    private int height;  // NOTE: height/rows are equivalent terminology
    private int width;   // NOTE: width/columns are equivalent terminology

    private Position2D camera = Position2D.origin();
    private Map<Position2D, MapEntity> entities = new HashMap<>();
    private GameColorSet gameColorSet;
    private Element element;

    /**
     * Any tiles that need re-rendering, as the game state has changed.
     */
    private List<Position2D> dirtyTiles = new ArrayList<>();

    public GameMap2D(String name, char[][] tileGrid, int height, int width) {
        this.name = name;
        this.tileGrid = tileGrid;
        this.height = height;
        this.width = width;
    }

    private GameMap2D() {
    }


    public GameMap2D createDeepCopy() {
        GameMap2D copy = new GameMap2D();

        copy.name = this.name;
        copy.tileGrid = this.getTileGridCopy();
        copy.height = this.height;
        copy.width = this.width;

        return copy;
    }

    public char[][] getTileGridCopy() {
        char[][] copy = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                copy[y][x] = tileGrid[y][x];
            }
        }
        return copy;
    }

    /**
     * Is the given origin within the bounds of the tileGrid?
     * @param py The y coordinate to check
     * @param px The x coordinate to check
     * @return True, if the origin lands on the tileGrid; otherwise, false.
     */
    public boolean withinBounds(int py, int px) {
        return (0 <= py && py < height) &&
                (0 <= px && px < width);
    }

    public boolean withinBounds(Position2D pos) {
        return withinBounds(pos.y(), pos.x());
    }

    /**
     * Is the given origin within the bounds of the tileGrid?
     * @param py The y coordinate to check
     * @param px The x coordinate to check
     * @return True, if the origin lands on the tileGrid; otherwise, false.
     */
    public boolean isPassable(int py, int px) {
        if(withinBounds(py, px)) {
            char sprite = tileGrid[py][px];
            return TileType.valueOf(sprite).getMetadata().isPassable();
        }
        return false;
    }

    public boolean isPassable(Position2D pos) {
        return isPassable(pos.y(), pos.x());
    }

    @Override
    public boolean isPassable(Position3D position) {
        return isPassable(position.to2D());
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return getName();
    }

    public List<Position2D> getDirtyTiles() {
        return dirtyTiles;
    }

    public char getTile(int y, int x) {
        return tileGrid[y][x];
    }

    public char getTile(Position2D pos) {
        return getTile(pos.y(), pos.x());
    }

    public void setTile(Position2D pos, TileType newTileType) {
        tileGrid[pos.y()][pos.x()] = newTileType.getSprite();
        dirtyTiles.add(pos);
    }

    public Region3D getMapRegionWith(Position3D origin) {
        return new Region3D(origin, width, height, 1);
    }

    /**
     * Get the top-left most origin of this map that matches the given character.
     *
     * @param character The type of cell to find`.
     * @return The top, left-most unvisited origin, or null if not found.
     */
    public Position2D findFirstOccurrence(char character) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tileGrid[y][x] == character) {
                    return new Position2D(x, y);
                }
            }
        }
        return null;
    }

    public List<Position2D> getMapRegion(Region2D region) {
        return Position2D.range(region);
    }

    @Override
    public TileType getTileType(int py, int px) {
        if (withinBounds(py, px)) {
            char sprite = tileGrid[py][px];
            return TileType.valueOf(sprite);
        } else {
            return TileType.EMPTY;
        }
    }

    @Override
    public TileType getTileType(Position2D p) {
        return getTileType(p.y(), p.x());
    }

    @Override
    public TileType getTileType(Position3D p) {
        return getTileType(p.y, p.x);
    }

    @Override
    public MapVoxel getVoxel(int x, int y, int z) {
        return getVoxel(new Position3D(x, y, z));
    }

    @Override
    public MapVoxel getVoxel(Position3D position) {
        TileType tileType = getTileType(position.y, position.x);
        return new MapVoxel(this, position, tileType);
    }

    public void addEntity(MapEntity entity) {
        Position2D position2D = entity.getPosition().to2D();
        this.entities.put(position2D, entity);
    }

    public boolean hasEntity(Position3D p) {
        Position2D position2D = p.to2D();
        return this.entities.containsKey(position2D);
    }

    public boolean hasItem(Position3D p) {
        return hasEntity(p) &&
                getEntity(p).getType() == EntityType.ITEM;
    }

    public MapEntity getEntity(Position3D p) {
        Position2D position2D = p.to2D();
        return this.entities.get(position2D);
    }

    public MapEntity removeEntity(Position3D position) {
        return this.entities.remove(position);
    }

    public void moveEntity(MapEntity entity, Position3D current) {
        Position3D previous = entity.getPosition();
        entities.remove(previous.to2D());
        entities.put(current.to2D(), entity);
        entity.setPosition(current);
    }


    public void update() {
        for(MapEntity entity: entities.values()) {
            entity.update();
        }
    }

    @Override
    public Position3D getCamera() {
        return camera.to3D();
    }

    @Override
    public void setCamera(Position3D camera) {
        this.camera = camera.to2D();
    }

    @Override
    public Position2D findFirstOccurrence2D(TileType tileType) {
        return findFirstOccurrence(tileType.getSprite());
    }

    @Override
    public Position3D findFirstOccurrence3D(TileType tileType) {
        return findFirstOccurrence(tileType.getSprite()).to3D();
    }

    public ArrayList<Position2D> getMapRegionMatching(Region2D region, Predicate<TileType> predicate) {
        return Position2D.range(region).stream()
                .filter(position -> predicate.test(getTileType(position)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Position2D> getPositionsMatching( Predicate<TileType> predicate) {
        Region2D region = new Region2D(0, 0, width, height);
        return getMapRegionMatching(region, predicate);
    }


    public String createTileDataString() {
        // go ahead and make room for spaces and newlines
        int len = (width * 2 + 1) * height;
        StringBuilder sb = new StringBuilder(len);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char next = tileGrid[y][x];
                sb.append(next);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String toString() {
        return createTileDataString();
    }

    public GameColorSet getGameColorSet() {
        return gameColorSet;
    }

    public void setGameColorSet(GameColorSet gameColorSet) {
        this.gameColorSet = gameColorSet;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Map<Position2D, TileType> getNeighbors(Position2D pos) {
        TreeMap<Position2D, TileType> neighbors = new TreeMap<>();
        Position2D one = new Position2D(1, 1);
        for (Position2D p : Position2D.range(pos.subtract(one), pos.add(one))) {
            if (p == pos) {
                continue;
            } else if (withinBounds(p)) {
                neighbors.put(p, getTileType(pos));
            } else {
                neighbors.put(p, TileType.EMPTY);
            }
        }
        return neighbors;
    }

    /**
     * Generates a list of positions most distant from the start position.
     * Only positions with the greatest local distance of its neighbors are
     * included. Positions with another position within a given distance are
     * excluded.
     *
     * @param start The starting position to calculate distances from.
     * @return The list of positions most distant from the start position.
     */
    public List<Position2D> findListOfDistantPositions(Position2D start) {
        Position2D startPos = start;

        // calculate distances from starting position for every open space
        HashMap<Position2D, Integer> distances = new HashMap<>();
        HashSet<Position2D> visited = new HashSet<>();
        Queue<Position2D> queue = new LinkedList<>();
        queue.add(startPos);
        visited.add(startPos);
        distances.put(startPos, 0);
        while (!queue.isEmpty()) {
            Position2D current = queue.poll();
            int nextDistance = distances.get(current) + 1;
            List<Position2D> neighbors = current.getNeighbors();
            for (Position2D neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    if (isPassable(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                        distances.put(neighbor, nextDistance);
                    }
                }
            }
        }

//        String distancesMapStr = getDistanceHashMapAsString(distances);
//        LOG.info("distancesMapStr:\n" + distancesMapStr);

        // function for filtering positions with the greatest local distance
        Predicate<Map.Entry<Position2D, Integer>> hasNoGreaterNeighbors = (e -> {
            Position2D current = e.getKey();
            int distance = e.getValue();
            List<Position2D> neighbors = current.getNeighbors();
            for (Position2D neighbor : neighbors) {
                if (distances.containsKey(neighbor)) {
                    int neighborDistance = distances.get(neighbor);
                    if (neighborDistance > distance) {
                        return false;
                    }
                }
            }
            return true;
        });

        // filter and sort positions with greatest local distance
        List<Position2D> distantPositions = distances.entrySet().stream()
                .filter(hasNoGreaterNeighbors)
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));

        // filter out positions with another position within a given distance.
        double minDistance = 3;
        Iterator<Position2D> distantPositionsIterator = distantPositions.iterator();
        while(distantPositionsIterator.hasNext()) {
            Position2D candidate = distantPositionsIterator.next();

            boolean skipCandidate = false;
            for (Position2D other : distantPositions) {
                double distance = candidate.distance(other);
                if (!candidate.equals(other) && distance <= minDistance) {
                    skipCandidate = true;
                    break;
                }
            }
            if (skipCandidate) {
                distantPositionsIterator.remove();
            }
        }

        return distantPositions;
    }

    /**
     * For debugging {@link #findListOfDistantPositions}.
     * @param distances The HashMap of distances for every open position.
     * @return A String representation the map printing the distance values
     *         for every Position2D present in the distances map.
     */
    private String getDistanceHashMapAsString(HashMap<Position2D, Integer> distances) {
        int maxDistance = distances.values().stream()
                .max(Comparator.comparingInt(Integer::intValue))
                .orElseThrow(() -> new IllegalStateException("Expected at least one value in distances map."));
        String maxDistanceStr = String.valueOf(maxDistance);
        int maxStrLength = maxDistanceStr.length();
        String formatStr = "%" + maxStrLength + "s ";

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char nextTile = tileGrid[y][x];
                String nextStr;
                Position2D nextPos = new Position2D(x, y);
                if (distances.containsKey(nextPos)) {
                    int distance = distances.get(nextPos);
                    nextStr = String.valueOf(distance);
                } else {
                    nextStr = String.valueOf(nextTile);
                }
                String nextAppendStr = String.format(formatStr, nextStr);
                sb.append(nextAppendStr);
                sb.append(' ');
            }
            sb.append('\n');
            sb.append('\n');
        }
        return sb.toString();
    }

}
