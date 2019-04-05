package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.map.MapType;
import com.aaroncarsonart.tarotrl.validation.ValidatedDefinition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent GameMaps in JSON definition files.
 *
 * Details are abstracted away from in-game use implementation
 * so that fields required to adequately describe map generation,
 * define entity population and behavior, etc can be added as
 * needed here without polluting the actual GameMap class files.
 *
 * This class represents one GameMap.  For the enclosing class
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameMapDefinition extends ValidatedDefinition {

    @NotNull
    private String mapName;

    @NotNull
    private MapType mapType;

    private String[] mapTerrainData = null;

    private Integer iterations = null;
    private Integer width = null;
    private Integer height = null;

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

    /**
     * Custom pre-normalization validation that ensures the data is described consistently
     * within JSON definition files.
     * @return The list of all validation errors.
     */
    @Override
    public List<String> validate() {
        List<String> validationErrors = new ArrayList<>();
        switch(mapType) {

            case VAULT:
                if (mapTerrainData == null || mapTerrainData.length == 0) {
                    validationErrors.add("VAULT mapTypes must define the mapTerrainData.");
                } else {
                    int expectedLength = mapTerrainData[0].length();
                    for (String row : mapTerrainData) {
                        if (expectedLength != row.length()) {
                            validationErrors.add("VAULT mapTypes must define the mapTerrainData " +
                                    "as an array of strings with even length (forming a rectangle).");
                            break;
                        }
                    }
                }
                if (width != null) {
                    validationErrors.add("VAULT mapTypes must not define a width. " +
                            "This property is calculated from the mapTerrainData.");
                }
                if (height != null) {
                    validationErrors.add("VAULT mapTypes must not define a height. " +
                            "This property is calculated from the mapTerrainData.");
                }
                break;

            case MAZE:
                if (mapTerrainData != null && mapTerrainData.length > 0) {
                    validationErrors.add("MAZE mapTypes must not define the mapTerrainData, " +
                            "as the map contents are generated later by the GameMapGenerator.");
                }
                if (width == null) {
                    validationErrors.add("MAZE mapTypes must define a width.");
                }
                if (height == null) {
                    validationErrors.add("MAZE mapTypes must define a height.");
                }
                break;

            case CELLULAR_AUTOMATA:
                if (mapTerrainData != null && mapTerrainData.length > 0) {
                    validationErrors.add("CELLULAR_AUTOMATA mapTypes must not define the mapTerrainData, " +
                            "as the map contents are generated later by the GameMapGenerator.");
                }
                if (iterations == null) {
                    validationErrors.add("CELLULAR_AUTOMATA mapTypes must define the iterations field.");
                }
                if (width == null) {
                    validationErrors.add("CELLULAR_AUTOMATA mapTypes must define a width.");
                }
                if (height == null) {
                    validationErrors.add("CELLULAR_AUTOMATA mapTypes must define a height.");
                }
                break;
        }

        return validationErrors;
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
                this.iterations = null;
                break;

            case MAZE:
                this.iterations = null;
                break;

            case CELLULAR_AUTOMATA:
                break;
        }
    }
}
