package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Difficulty;
import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.deck.TarotCardType;
import com.aaroncarsonart.tarotrl.deck.TarotDeck;
import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.graphics.GameColors;
import com.aaroncarsonart.tarotrl.inventory.GameItem;
import com.aaroncarsonart.tarotrl.inventory.Treasure;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.RNG;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;
import com.aaroncarsonart.tarotrl.world.WorldVoxel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Generate GameWorld instances for the player to explore.
 */
public class GameWorldGenerator {
    private static final Logger LOG = new Logger(GameWorldGenerator.class).withLogLevel(LogLevel.INFO);

    private JsonDefinitionLoader loader;
    private GameMapGenerator generator;

    public GameWorldGenerator() {
        loader = new JsonDefinitionLoader();
        generator = new GameMapGenerator();
    }

    private boolean mapRegionFitsGameWorld(GameWorld world, GameMap map, Position3D mapOrigin) {
        Region3D mapRegion = map.getMapRegionWith(mapOrigin);
        return world.voxelRegionMatches(mapRegion, WorldVoxel::isUndefined);
    }

    private void putMap(GameWorld world, GameMap map, Position3D origin) {
        Position3D dimensions = new Position3D(map.getWidth(), map.getHeight(), 1);
        Position3D max = origin.add(dimensions);
        Position3D.forEach(origin, max, position3D -> {
            WorldVoxel voxel = world.getVoxel(position3D);
            char sprite = map.getTile(position3D.subtract(origin).to2D());
            TileType tileType = TileType.valueOf(sprite);
            voxel.setTileType(tileType);
        });
    }

    public GameWorld generateMazeWorld() {
        return generateDescendingMazeWorld();
    }


    public GameWorld generateImbroglioWorld() {
        return generateDescendingMazeWorld();
    }

    private GameWorld generateCarsonFamilyHome() {
        LOG.info("Generating Carson family home map ...");
        GameWorld world = new GameWorld();

        String pathLevel1 = "/maps/vault_parents_house_lv_1.json";
        String pathLevel2 = "/maps/vault_parents_house_lv_2.json";
        String pathBasement = "/maps/vault_parents_house_basement.json";

        GameMapDefinition level1Definition = loader.loadGameMapDefinition(pathLevel1);
        GameMapDefinition level2Definition = loader.loadGameMapDefinition(pathLevel2);
        GameMapDefinition basementDefinition = loader.loadGameMapDefinition(pathBasement);

        GameMap mapLevel1 = generator.generateMapFrom(level1Definition);
        GameMap mapLevel2 = generator.generateMapFrom(level2Definition);
        GameMap mapBasement = generator.generateMapFrom(basementDefinition);

        Position3D start = mapLevel1.findFirstOccurrence(TileType.UPSTAIRS.getSprite()).to3D()
                .withRelativeY(-1);
        world.setCamera(start);

        // put level 1
        Position3D level1Origin = Position3D.ORIGIN;
        putMap(world, mapLevel1, level1Origin);

        // put level 2
        Position2D level1Upstairs = mapLevel1.findFirstOccurrence(TileType.UPSTAIRS.getSprite());
        Position2D level2Downstairs = mapLevel2.findFirstOccurrence(TileType.DOWNSTAIRS.getSprite());

        Position3D level2Origin = level1Upstairs
                .subtract(level2Downstairs)
                .to3D(level1Origin.z + 1);
        putMap(world, mapLevel2, level2Origin);

        // put basement
        Position2D level1Downstairs = mapLevel1.findFirstOccurrence(TileType.DOWNSTAIRS.getSprite());
        Position2D basementUpstairs = mapBasement.findFirstOccurrence(TileType.UPSTAIRS.getSprite());

        Position3D basementOrigin = level1Downstairs
                .subtract(basementUpstairs)
                .to3D(level1Origin.z - 1);
        putMap(world, mapBasement, basementOrigin);

        return world;
    }

    private GameWorld generateDescendingMazeWorld() {
        LOG.info("Generating maze maps ...");

        GameWorld world = new GameWorld();
        int levelCount = 10;

        Difficulty difficulty = Difficulty.NORMAL;
        Position3D prevStairs = Position3D.ORIGIN;

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {

            int width = 5 + level+ RNG.nextInt(13 + 2 * level);
            int height = 5 + level + RNG.nextInt(13 + 2 * level);

            LOG.debug("generating level %d maze: %d x %d", level, width, height);

            Maze maze = Maze.generateRandomWalledMaze(width, height);
            maze.setDifficulty(difficulty);

            GameMap map = GameMapGenerator.generateMapFrom(maze);
            map.setName("Level " + level);

            ArrayList<Position2D> mapPaths = map.getPositionsMatching(t -> t.equals(TileType.PATH));
            RNG.shuffle(mapPaths);

            Position3D mapOrigin;
            Position2D upstairs = mapPaths.remove(0);
            Position2D downstairs = mapPaths.remove(0);

            if (level == 1) {
                Position3D startingPosition = new Position3D(1, 1, 0);
                world.setCamera(startingPosition);
                mapOrigin = Position3D.ORIGIN;
                map.setTile(downstairs, TileType.DOWNSTAIRS);
            } else if (level < levelCount) {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
                map.setTile(downstairs, TileType.DOWNSTAIRS);
            } else {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
            }

            // draw map
            putMap(world, map, mapOrigin);

            // add treasures
            int treasureCount = (width + height) / 4 - 3;
            for (int i = 0; i < treasureCount; i++) {
                Position3D position = mapPaths.remove(0).to3D();
                position = mapOrigin.add(position);

                Treasure treasure = new Treasure();
                ItemEntity entity = new ItemEntity(TileType.TREASURE, position, treasure);
                world.addEntity(entity);
            }

            // update prevStairs
            prevStairs = mapOrigin.add(downstairs.to3D()).moveTowards(Direction3D.BELOW);
        }

        // add special items
        ArrayList<Position3D> worldPaths = world.getWorldMap().values().stream()
                .filter(v -> v.getTileType() == TileType.PATH)
                .filter(v -> v.world.getEntity(v.position) == null)
                .map(v -> v.position)
                .collect(Collectors.toCollection(ArrayList::new));

        RNG.shuffle(worldPaths);

        TarotDeck tarotDeck = loader.loadTarotDeck();
        List<TarotCard> tarotCards = tarotDeck.getSetOfCards();
        TileDefinition tarotCardTile = TileDefinition.custom('☼',
                TileType.ITEM,
                GameColors.MAGENTA,
                TileType.ITEM.getMetadata().getBackgroundColor());

        for (TarotCard card : tarotCards) {
            Position3D position = worldPaths.remove(0);

            TarotCardType cardType = card.getTarotCardType();

            String name = "the \"" + card.getDisplayName() + "\" Tarot Card";
            String description = "the face of the card is shining with a brilliant " +
                    cardType.colorName + "ish sheen.";

            GameItem item = new GameItem(name, description);

            String entityDescription = "there lies a mysterious item of immense power";
            ItemEntity entity = new ItemEntity(tarotCardTile, position, item, entityDescription);
            world.addEntity(entity);
        }
        return world;
    }

    public GameWorld generateCavernWorld() {
        LOG.info("Generating Cavern maps ...");

        GameWorld world = new GameWorld();
        int levelCount = 25;
        int scaleFactor = 23 - levelCount / 2;

        ToIntFunction<Integer> mapDimensionCalulator = level ->
                2 * (5 + level + RNG.nextInt(scaleFactor + 11 * level));

        Position3D prevStairs = Position3D.ORIGIN;

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {

            int width =  mapDimensionCalulator.applyAsInt(level);
            int height = mapDimensionCalulator.applyAsInt(level);

            LOG.debug("generating level %d cavern map: (%d x %d)", level, width, height);

            Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
            GameMap map = GameMapGenerator.generateMapFrom(maze);
            map.setName("Level " + level);

            ArrayList<Position2D> mapPaths = map.getPositionsMatching(t -> t.equals(TileType.PATH));
            RNG.shuffle(mapPaths);

            Position3D mapOrigin;
            Position2D upstairs = mapPaths.remove(0);
            Position2D downstairs = mapPaths.remove(0);

            if (level == 1) {
                Position3D startingPosition = new Position3D(1, 1, 0);
                world.setCamera(startingPosition);
                mapOrigin = Position3D.ORIGIN;
                map.setTile(downstairs, TileType.DOWNSTAIRS);
            } else if (level < levelCount) {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
                map.setTile(downstairs, TileType.DOWNSTAIRS);
            } else {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
            }

            // draw map
            putMap(world, map, mapOrigin);

            // add treasures
            int treasureCount = (width + height) / 10 - 3;
            for (int i = 0; i < treasureCount; i++) {
                Position3D position = mapPaths.remove(0).to3D();
                position = mapOrigin.add(position);

                Treasure treasure = new Treasure();
                ItemEntity entity = new ItemEntity(TileType.TREASURE, position, treasure);
                world.addEntity(entity);
            }

            // update prevStairs
            prevStairs = mapOrigin.add(downstairs.to3D()).moveTowards(Direction3D.BELOW);
        }

        // add special items
        ArrayList<Position3D> worldPaths = world.getWorldMap().values().stream()
                .filter(v -> v.getTileType() == TileType.PATH)
                .filter(v -> v.world.getEntity(v.position) == null)
                .map(v -> v.position)
                .collect(Collectors.toCollection(ArrayList::new));

        RNG.shuffle(worldPaths);

        TarotDeck tarotDeck = loader.loadTarotDeck();
        List<TarotCard> tarotCards = tarotDeck.getSetOfCards();
        TileDefinition tarotCardTile = TileDefinition.custom('☼',
                TileType.ITEM,
                GameColors.MAGENTA,
                TileType.ITEM.getMetadata().getBackgroundColor());

        for (TarotCard card : tarotCards) {
            Position3D position = worldPaths.remove(0);

            TarotCardType cardType = card.getTarotCardType();

            String name = "the \"" + card.getDisplayName() + "\" Tarot Card";
            String description = "the face of the card is shining with a brilliant " +
                    cardType.colorName + "ish sheen.";

            GameItem item = new GameItem(name, description);

            String entityDescription = "there lies a mysterious item of immense power";
            ItemEntity entity = new ItemEntity(tarotCardTile, position, item, entityDescription);
            world.addEntity(entity);
        }
        return world;
    }

}
