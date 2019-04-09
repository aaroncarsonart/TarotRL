package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.map.json.deserializer.TileDefinitionMapDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class TileDefinitionSet {

    private String name;

    @JsonDeserialize(using = TileDefinitionMapDeserializer.class)
    private Map<Character, TileDefinition> tileDefinitions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Character, TileDefinition> getTileDefinitions() {
        return tileDefinitions;
    }

    public void setTileDefinitions(Map<Character, TileDefinition> tileDefinitions) {
        this.tileDefinitions = tileDefinitions;
    }
}
