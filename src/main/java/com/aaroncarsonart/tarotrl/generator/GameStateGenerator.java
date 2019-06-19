package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.GameMap3D;

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
