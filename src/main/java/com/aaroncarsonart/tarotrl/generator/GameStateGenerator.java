package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.tarotrl.deck.Element;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.graphics.GameColorSet;
import com.aaroncarsonart.tarotrl.map.GameMap2D;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.RNG;
import com.aaroncarsonart.tarotrl.world.GameMap3D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameStateGenerator {
    private static final Logger LOG = new Logger(GameStateGenerator.class);

    private JsonDefinitionLoader loader;
    private GameWorldGenerator gameWorldGenerator;

    public GameStateGenerator() {
        loader = new JsonDefinitionLoader();
        gameWorldGenerator = new GameWorldGenerator();
    }

    public static void setTileTypeMetadata(TileDefinitionSet tileDefinitionSet) {
        Map<Character, TileDefinition> definitions = tileDefinitionSet.getTileDefinitions();
        for (TileType tileType : TileType.values()) {
            Character sprite = tileType.getSprite();
            TileDefinition definition = definitions.get(sprite);
            tileType.setMetadata(definition);
        }
    }

    public GameState generateImbroglioGameState() {
        LOG.info("Generating Imbroglio GameState ...");
        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/imbroglio.json");
        setTileTypeMetadata(tileDefinitionSet);

        GameState gameState = new GameState();

        GameMap3D world = gameWorldGenerator.generateImbroglioWorld();
        gameState.setGameMap(world);
        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.EMPTY);
        gameState.gainTreasure(50);

        return gameState;
    }

    public GameState generateTarotRLGameState2() {
        LOG.info("Generating TarotRL GameState ...");
        GameState gameState = new GameState();

//        GameMap3D gameMap = gameWorldGenerator.generateCavernWorld();
//        gameState.setGameMap(gameMap);

//        Maze maze = Maze.generateRandomWalledMaze(30, 20);
//        maze.setDifficulty(Difficulty.NORMAL);
////        GameMap gameMap = GameMapGenerator.generateMapFrom(maze);
////        Position3D camera = gameMap.findFirstOccurrence3D(TileType.PATH);
//        gameMap.setCamera(camera);
//        gameState.setGameMap(gameMap);

        List<GameMap2D> gameMaps = new ArrayList<>();

//        TileDefinitionSet greenTileSet = loader.loadTileDefinitionSet("tile_definitions/forest_green.json");
//        TileDefinitionSet redTileSet = loader.loadTileDefinitionSet("tile_definitions/mountain_red.json");

        int width = 30;
        int height = 20;

        List<Pair<Element, GameColorSet>> pairs = Arrays.asList(
                new Pair<>(Element.EARTH, GameColorSet.GREEN),
                new Pair<>(Element.AIR, GameColorSet.YELLOW),
                new Pair<>(Element.WATER, GameColorSet.CYAN),
                new Pair<>(Element.FIRE, GameColorSet.RED),
                new Pair<>(Element.SPIRIT, GameColorSet.MAGENTA));

        for (Pair<Element, GameColorSet> pair: pairs) {
            Element element = pair.getKey();
            GameColorSet colorSet = pair.getValue();
            if (element == Element.SPIRIT) {
                // TODO vary tile definition sets per map element
                for (int i = 0; i <= 21; i++) {
                    Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
                    GameMap2D map = GameMapGenerator.generateMapFrom(maze);
                    map.setName(element.getCardType().name() + " " + i);
                    map.setElement(element);
                    map.setGameColorSet(colorSet);
                    gameMaps.add(map);
                }
            } else {
                // TODO vary tile definition sets per map
                for (int i = 1; i <= 14; i++) {
                    Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
                    GameMap2D map = GameMapGenerator.generateMapFrom(maze);
                    map.setName(element.getCardType().name() + " " + i);
                    map.setElement(element);
                    map.setGameColorSet(colorSet);
                    gameMaps.add(map);
                }
            }
        }
        // Add the fool to the end
        Maze maze = GameMapGenerator.generateCellularAutomataRoom(width, height, 3);
        GameMap2D map = GameMapGenerator.generateMapFrom(maze);
        map.setName("The Fool");
        map.setElement(Element.SPIRIT);
        map.setGameColorSet(GameColorSet.GREY);
        gameMaps.add(map);


        RNG.shuffle(gameMaps);
        // TODO: link all maps up with stairs connecting via tarot cards


        // TODO  create mechanism for adding 2-way portals '%' between levels


        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.WALL);

        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        return gameState;
    }

    public GameState generateTarotRLGameState() {
        LOG.info("Generating TarotRL GameState ...");
//        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/forest_green.json");
//        setTileTypeMetadata(tileDefinitionSet);

        GameState gameState = new GameState();

        GameMap3D gameMap = gameWorldGenerator.generateCavernWorld();
        gameState.setGameMap(gameMap);

//        Maze maze = Maze.generateRandomWalledMaze(30, 20);
//        maze.setDifficulty(Difficulty.NORMAL);
////        GameMap gameMap = GameMapGenerator.generateMapFrom(maze);
////        Position3D camera = gameMap.findFirstOccurrence3D(TileType.PATH);
//        gameMap.setCamera(camera);
//        gameState.setGameMap(gameMap);

        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.WALL);


        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        return gameState;
    }
}
