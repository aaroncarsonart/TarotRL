package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.world.GameWorld;

import java.util.Map;

public class GameStateGenerator {
    private static final Logger LOG = new Logger(GameStateGenerator.class);

    private JsonDefinitionLoader loader;
    private GameWorldGenerator gameWorldGenerator;

    public GameStateGenerator() {
        loader = new JsonDefinitionLoader();
        gameWorldGenerator = new GameWorldGenerator();
    }

    private void setTileTypeMetadata(TileDefinitionSet tileDefinitionSet) {
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

        GameWorld world = gameWorldGenerator.generateImbroglioWorld();
        gameState.setGameWorld(world);
        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.EMPTY);
        gameState.gainTreasure(50);

        return gameState;
    }

    public GameState generateTarotRLGameState() {
        LOG.info("Generating TarotRL GameState ...");
        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("tile_definitions/forest_green.json");
        setTileTypeMetadata(tileDefinitionSet);

        GameState gameState = new GameState();

        GameWorld world = gameWorldGenerator.generateCavernWorld();
        gameState.setGameWorld(world);
        gameState.toggleAutoCollect();
        gameState.setUndefinedTileType(TileType.WALL);

        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        return gameState;
    }
}
