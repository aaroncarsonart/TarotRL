package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Difficulty;
import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.deck.TarotCardType;
import com.aaroncarsonart.tarotrl.deck.TarotDeck;
import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.graphics.GameColors;
import com.aaroncarsonart.tarotrl.inventory.Item;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.inventory.Treasure;
import com.aaroncarsonart.tarotrl.map.GameMap2D;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.RNG;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Generate GameMap3D instances for the player to explore.
 */
public class GameWorldGenerator {
    private static final Logger LOG = new Logger(GameWorldGenerator.class).withLogLevel(LogLevel.INFO);

    private JsonDefinitionLoader loader;
    private GameMapGenerator generator;

    public GameWorldGenerator() {
        loader = new JsonDefinitionLoader();
        generator = new GameMapGenerator();
    }

    private boolean mapRegionFitsGameWorld(GameMap3D world, GameMap2D map, Position3D mapOrigin) {
        Region3D mapRegion = map.getMapRegionWith(mapOrigin);
        return world.voxelRegionMatches(mapRegion, MapVoxel::isUndefined);
    }

    private void putMap(GameMap3D world, GameMap2D map, Position3D origin) {
        Position3D dimensions = new Position3D(map.getWidth(), map.getHeight(), 1);
        Position3D max = origin.add(dimensions);
        Position3D.forEach(origin, max, position3D -> {
            MapVoxel voxel = world.getVoxel(position3D);
            char sprite = map.getTile(position3D.subtract(origin).to2D());
            TileType tileType = TileType.valueOf(sprite);
            voxel.setTileType(tileType);
        });
    }

    public GameMap3D generateMazeWorld() {
        return generateDescendingMazeWorld();
    }


    public GameMap3D generateImbroglioWorld() {
        return generateDescendingMazeWorld();
    }

    private GameMap3D generateCarsonFamilyHome() {
        LOG.info("Generating Carson family home map ...");
        GameMap3D world = new GameMap3D();

        String pathLevel1 = "/maps/vault_parents_house_lv_1.json";
        String pathLevel2 = "/maps/vault_parents_house_lv_2.json";
        String pathBasement = "/maps/vault_parents_house_basement.json";

        GameMapDefinition level1Definition = loader.loadGameMapDefinition(pathLevel1);
        GameMapDefinition level2Definition = loader.loadGameMapDefinition(pathLevel2);
        GameMapDefinition basementDefinition = loader.loadGameMapDefinition(pathBasement);

        GameMap2D mapLevel1 = generator.generateMapFrom(level1Definition);
        GameMap2D mapLevel2 = generator.generateMapFrom(level2Definition);
        GameMap2D mapBasement = generator.generateMapFrom(basementDefinition);

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

    // TODO clean up code duplication. Possibly delete.
    // TODO (Uses old tarot card item implementation)
    private GameMap3D generateDescendingMazeWorld() {
        LOG.info("Generating maze maps ...");

        GameMap3D world = new GameMap3D();
        int levelCount = 10;

        Difficulty difficulty = Difficulty.NORMAL;
        Position3D prevStairs = Position3D.ORIGIN;

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {

            int width = 5 + level + RNG.nextInt(13 + 2 * level);
            int height = 5 + level + RNG.nextInt(13 + 2 * level);

            LOG.debug("generating level %d maze: %d x %d", level, width, height);

            Maze maze = Maze.generateRandomWalledMaze(width, height);
            maze.setDifficulty(difficulty);

            GameMap2D map = GameMapGenerator.generateMapFrom(maze);
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
            int treasureCount = (width + height) / 4;
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
                .filter(v -> v.map.getEntity(v.position) == null)
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

            Item item = new Item(name, description);

            String entityDescription = "there lies a mysterious item of immense power";
            ItemEntity entity = new ItemEntity(tarotCardTile, position, item, entityDescription);
            world.addEntity(entity);
        }
        return world;
    }

    // TODO clean up code duplication. Possibly delete.
    // TODO (Uses old tarot card item implementation)
    public GameMap3D generateCavernWorld() {
        LOG.info("Generating Cavern maps ...");

        GameMap3D world = new GameMap3D();
        int levelCount = 10;
        int scaleFactor = 11 - levelCount / 2;

        ToIntFunction<Integer> mapDimensionCalulator = level ->
                2 * (5 + level + RNG.nextInt(scaleFactor + 11 * level));

        Position3D prevStairs = Position3D.ORIGIN;

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {

            int width =  mapDimensionCalulator.applyAsInt(level);
            int height = mapDimensionCalulator.applyAsInt(level);

            LOG.debug("generating level %d cavern map: (%d x %d)", level, width, height);

            Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
            GameMap2D map = GameMapGenerator.generateMapFrom(maze);
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
                .filter(v -> v.map.getEntity(v.position) == null)
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

            Item item = new Item(name, description);

            String entityDescription = "there lies a mysterious item of immense power";
            ItemEntity entity = new ItemEntity(tarotCardTile, position, item, entityDescription);
            world.addEntity(entity);
        }
        return world;
    }

    public GameMap3D generateTarotRLCavernWorld() {
        LOG.info("Generating Cavern maps ...");

        GameMap3D world = new GameMap3D();
        int levelCount = 6;

        Position3D prevStairs = Position3D.ORIGIN;

        int width = 30;
        int height = 20;

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {


            LOG.debug("generating level %d cavern map: (%d x %d)", level, width, height);

            Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
            GameMap2D map = GameMapGenerator.generateMapFrom(maze);
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
                .filter(v -> v.map.getEntity(v.position) == null)
                .map(v -> v.position)
                .collect(Collectors.toCollection(ArrayList::new));

        RNG.shuffle(worldPaths);

        // TODO: associate Tarot Cards with levels
        TarotDeck tarotDeck = loader.loadTarotDeck();
        List<TarotCard> tarotCards = tarotDeck.getSetOfCards();
        TileDefinition tarotCardTile = TileDefinition.custom('☼',
                TileType.ITEM,
                GameColors.MAGENTA,
                TileType.ITEM.getMetadata().getBackgroundColor());

        for (TarotCard card : tarotCards) {
            Position3D position = worldPaths.remove(0);
            Item item = new TarotCardItem(card);

            String entityDescription = "there lies a mysterious item of immense power";
            ItemEntity entity = new ItemEntity(tarotCardTile, position, item, entityDescription);
            world.addEntity(entity);
        }
        return world;
    }

    public static void main(String[] args) {
        // test search algorithm.
        Position2D test = new Position2D(1,2);
        Position2D test2 = new Position2D(1,2);

        System.out.println(test.hashCode() == test2.hashCode());

    }
}
