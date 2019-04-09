package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import com.aaroncarsonart.tarotrl.world.GameWorld;

import java.util.Map;

public class GameStateGenerator {

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

    public GameState generateInitialGameState() {
        GameState gameState = new GameState();

        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("/tile_definitions/forest_green.json");
        gameState.setTileDefinitions(tileDefinitionSet.getTileDefinitions());
        setTileTypeMetadata(tileDefinitionSet);

        GameWorld world = gameWorldGenerator.generateGameWorld();
        gameState.setGameWorld(world);

        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        return gameState;
    }

    public GameState generateInitialGameState_GameMap() {
        GameState gameState = new GameState();

        TileDefinitionSet tileDefinitionSet = loader.loadTileDefinitionSet("/tile_definitions/forest_green.json");
        gameState.setTileDefinitions(tileDefinitionSet.getTileDefinitions());
        setTileTypeMetadata(tileDefinitionSet);

        // TODO: remove GameMaps from GameState.
        GameMapGenerator generator = new GameMapGenerator();
        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house_lv_1.json");
        GameMap gameMap = generator.generateMapFrom(definition);

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        String initialStatus = "Welcome to TarotRL!!!";
        gameState.setStatus(initialStatus);

        Position2D start = gameMap.findFirstOccurrence(TileType.PATH.getSprite());
        gameState.setPlayerPosition(start);

        return gameState;
    }

}
