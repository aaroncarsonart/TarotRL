package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.imbroglio.Difficulty;
import com.aaroncarsonart.tarotrl.map.MapType;
import com.aaroncarsonart.tarotrl.validation.ValidatedDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent GameMaps in JSON definition files.
 *
 * Details are abstracted away from in-game use implementation
 * so that fields required to adequately describe map generation,
 * define entity population and behavior, etc can be added as
 * needed here without polluting the actual GameMap class files.
 *
 * This class represents one GameMap.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameMapDefinition extends ValidatedDefinition {

    @NotNull
    private String mapName;

    @NotNull
    private MapType mapType;

    private String[] mapTerrainData = null;

    private Integer width = null;
    private Integer height = null;

    private Character backgroundSprite = null;

    /**
     * Easier mazes have more disconnected components, and thus have
     * multiple paths to reach most destinations within the maze.
     */
    private Difficulty mazeDifficulty = Difficulty.HARD;

    /**
     * The number of additional iterations applied to CELLULAR_AUTOMATA maps.
     */
    private Integer iterations = null;

    /**
     * What digPercentage to dig RANDOM maps before stopping.
     */
    private Double digPercentage = null;

    /**
     * What digPercentage to dig RANDOM maps before stopping.
     */
    private Boolean useTunnels = null;

    private List<TileDefinition> spriteKey;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public String[] getMapTerrainData() {
        return mapTerrainData;
    }

    public void setMapTerrainData(String[] mapTerrainData) {
        this.mapTerrainData = mapTerrainData;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        this.iterations = iterations;
    }

    public Double getDigPercentage() {
        return digPercentage;
    }

    public void setDigPercentage(Double digPercentage) {
        this.digPercentage = digPercentage;
    }

    public Boolean getUseTunnels() {
        return useTunnels;
    }

    public void setUseTunnels(Boolean useTunnels) {
        this.useTunnels = useTunnels;
    }

    public Difficulty getMazeDifficulty() {
        return mazeDifficulty;
    }

    public void setMazeDifficulty(Difficulty mazeDifficulty) {
        this.mazeDifficulty = mazeDifficulty;
    }

    public Character getBackgroundSprite() {
        return backgroundSprite;
    }

    public void setBackgroundSprite(Character backgroundSprite) {
        this.backgroundSprite = backgroundSprite;
    }

    public List<TileDefinition> getSpriteKey() {
        return spriteKey;
    }

    public void setSpriteKey(List<TileDefinition> spriteKey) {
        this.spriteKey = spriteKey;
    }

    /**
     * Custom pre-normalization validation that ensures the data is described consistently
     * within JSON definition files.
     * @return The list of all validation errors.
     */
    @Override
    public List<String> validate() {
        return new ArrayList<>();
    }

    /**
     * Based on the {@link #mapType}, this method will will assign default values
     * nullify unused fields, to ensure data consistency.
     *
     * This method assumes the normalized data has been asserted valid by the
     * {@link com.aaroncarsonart.tarotrl.validation.DefinitionValidator DefinitionValidator}.
     */
    public void normalize() {
        switch(mapType) {
            case VAULT:
                int charactersPerRow = mapTerrainData[0].length();
                int rowCount = mapTerrainData.length;
                this.width = charactersPerRow;
                this.height = rowCount;
                break;
        }
    }

    /**
     * Create a Map of sprite to definitions for the spriteKey.
     * @return A
     */
    public Map<Character, TileDefinition> createSpriteToTile() {
        HashMap<Character, TileDefinition> spriteToTileDefinition = new HashMap<>();
        for (TileDefinition definition: spriteKey) {
            spriteToTileDefinition.put(definition.getSprite(), definition);
        }
        return spriteToTileDefinition;
    }
}
