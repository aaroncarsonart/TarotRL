package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import com.aaroncarsonart.tarotrl.world.GameWorld;
import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.world.WorldVoxel;

/**
 * Generate GameWorld instances for the player to explore.
 */
public class GameWorldGenerator {

    private JsonDefinitionLoader loader;
    private GameMapGenerator generator;

    public GameWorldGenerator() {
        loader = new JsonDefinitionLoader();
        generator = new GameMapGenerator();
    }

    public GameWorld generateGameWorld() {
        GameWorld gameWorld = new GameWorld();
        generateCarsonFamilyHome(gameWorld);
        return gameWorld;
    }

    public void generateCarsonFamilyHome(GameWorld world) {
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
}
