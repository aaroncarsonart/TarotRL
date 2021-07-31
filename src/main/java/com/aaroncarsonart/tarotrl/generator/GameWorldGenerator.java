package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Difficulty;
import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.deck.TarotCard;
import com.aaroncarsonart.tarotrl.deck.TarotCardType;
import com.aaroncarsonart.tarotrl.deck.TarotDeck;
import com.aaroncarsonart.tarotrl.entity.ItemEntity;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.graphics.GameColors;
import com.aaroncarsonart.tarotrl.inventory.Item;
import com.aaroncarsonart.tarotrl.inventory.TarotCardItem;
import com.aaroncarsonart.tarotrl.inventory.Treasure;
import com.aaroncarsonart.tarotrl.map.GameMap2D;
import com.aaroncarsonart.tarotrl.map.TileDefinitionSets;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.LogLevel;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.RNG;
import com.aaroncarsonart.tarotrl.world.Direction3D;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import com.aaroncarsonart.tarotrl.world.MapVoxel;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.Region3D;
import org.hexworks.zircon.api.color.TileColor;

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

    /**
     * Generate a GameMap3D based on the input generatorCard.
     * @param gameState The GameState in which to store the GameMap3D data.
     * @param generatorCard The TarotCard off of which to generate the map from.
     * @return a GameMap3D based on the input generatorCard.
     */
    public GameMap3D generateTarotRLGameMap(GameState gameState, TarotCard generatorCard) {
        LOG.info("generatorCard: " + generatorCard.getDisplayName());
        GameMap3D world = new GameMap3D();

        TarotCardType cardType = generatorCard.getTarotCardType();
        TileDefinitionSet tileDefinitionSet = TileDefinitionSets.getTileDefinitionSetFor(cardType);
        world.setTileDefinitionSet(tileDefinitionSet);
        TileType.setTileTypeMetadata(tileDefinitionSet);

        Position3D prevStairs = Position3D.ORIGIN;
        int levelCount = Math.max(1, generatorCard.getRank());
        LOG.info("levelCount: " + levelCount);

        // generate the descending levels of the Maze
        for (int level = 1; level <= levelCount; level ++) {
            double xScale = RNG.nextDouble(1, 6);
            double yScale = RNG.nextDouble(1, 6);

            int width = (int) (15 * xScale);
            int height = (int) (10 * yScale);

            LOG.info("----------------------------------------------");
            LOG.info("generating level %d cavern map: (%d x %d)", level, width, height);

            // ensure the generated maze contains paths
            Maze maze;
            do {
                maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
            } while (!maze.toBorderedString('.', '#').contains("."));
            GameMap2D map = GameMapGenerator.generateMapFrom(maze);
            map.setName("Level " + level);

            ArrayList<Position2D> mapPaths = map.getPositionsMatching(t -> t.equals(TileType.PATH));
            RNG.shuffle(mapPaths);

            Position3D mapOrigin;
            Position2D upstairs = mapPaths.remove(0);
            Position2D downstairs = mapPaths.remove(0);
            Position2D entryPosition;

            if (level == 1) {
//                Position3D startingPosition = map.findFirstOccurrence2D(TileType.PATH).to3D();
//                Position3D startingPosition = new Position3D(1, 1, 0);
                Position3D startingPosition = mapPaths.remove(0).to3D(0);
                entryPosition = startingPosition.to2D();
                LOG.info("startingPosition: " + startingPosition);

                world.setCamera(startingPosition);
                mapOrigin = Position3D.ORIGIN;

                // Don't add stairs for single-level maps
                if (levelCount != 1) {
                    map.setTile(downstairs, TileType.DOWNSTAIRS);
                }
            } else if (level < levelCount) {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
                map.setTile(downstairs, TileType.DOWNSTAIRS);
                entryPosition = upstairs;
            } else {
                mapOrigin = prevStairs.subtract(upstairs.to3D());
                map.setTile(upstairs, TileType.UPSTAIRS);
                entryPosition = upstairs;
            }
            LOG.info("mapOrigin: " + mapOrigin);

            // draw map
            putMap(world, map, mapOrigin);
            LOG.info("map level z: " + mapOrigin.z);

            // get most distant paths
            List<Position2D> distantPaths = map.findListOfDistantPositions(entryPosition);
            distantPaths.remove(mapOrigin);
            distantPaths.remove(upstairs);
            distantPaths.remove(downstairs);
            mapPaths.removeAll(distantPaths);

            // add tarot card to bottom level of world
            if (level == levelCount) {
                TarotDeck tarotDeck = gameState.getAllTarotCards();
                TarotCard cardToFind = tarotDeck.drawTopCard();
                Position3D cardPos = distantPaths.remove(0).to3D();
                cardPos = cardPos.add(mapOrigin);
                Item cardToFindItem = new TarotCardItem(cardToFind);
                TileColor cardColor = cardToFind.getTarotCardType().symbolColor;
                TileDefinition tarotCardTile = TileDefinition.custom('☼',
                        TileType.ITEM,
                        cardColor,
                        TileType.ITEM.getMetadata().getBackgroundColor());

                String entityDescription = "there lies a mysterious item of immense power";
                ItemEntity entity = new ItemEntity(tarotCardTile, cardPos, cardToFindItem, entityDescription);
                world.addEntity(entity);
            }

            // add treasures
            int treasureCount = (width + height) / 10 - 3;
            for (int i = 0; i < treasureCount; i++) {
                Position3D position;
                if (RNG.nextInt(0, 3) == 0 && !distantPaths.isEmpty()) {
                    int randomIndex = RNG.nextInt(distantPaths.size());
                    position = distantPaths.remove(randomIndex).to3D();
                } else {
                    position = mapPaths.remove(0).to3D();
                }

                position = mapOrigin.add(position);

                Treasure treasure = new Treasure();
                ItemEntity entity = new ItemEntity(TileType.TREASURE, position, treasure);
                world.addEntity(entity);
            }

//            // add all other distant paths as simple items
//            for (Position2D distantPath : distantPaths) {
//                Position3D itemPos = distantPath.to3D().add(mapOrigin);
//                Item item = new Item("Hat", "it's a nice hat");
//                ItemEntity entity = new ItemEntity(TileType.ITEM, itemPos, item);
//                world.addEntity(entity);
//            }
//            distantPaths.clear();

            // update prevStairs
            prevStairs = mapOrigin.add(downstairs.to3D()).moveTowards(Direction3D.BELOW);
        }

        // add tarot card to bottom level
//        int bottomLevel = 1 - levelCount;
//        LOG.info("bottomLevel: " + bottomLevel);
//        ArrayList<Position3D> bottomLevelPositions = world.getWorldMap().values().stream()
//                .filter(v -> v.getTileType() == TileType.PATH)
//                .filter(v -> v.map.getEntity(v.position) == null)
//                .map(v -> v.position)
//                .filter(v -> v.z == bottomLevel)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        RNG.shuffle(bottomLevelPositions);

        return world;
    }

    public static void main(String[] args) {
        // test search algorithm.
        Position2D test = new Position2D(1,2);
        Position2D test2 = new Position2D(1,2);

        System.out.println(test.hashCode() == test2.hashCode());

    }
}
