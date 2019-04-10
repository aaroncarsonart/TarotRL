package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.tarotrl.map.json.TileDefinition;

import java.util.HashMap;
import java.util.Map;

public enum TileType {
    PLAYER('@', "you have found yourself"),
    WALL(  '#', "there is a wall"),
    PATH(  '.', "there is an open path"),
    EMPTY( ' ', "there is nothing"),

    DOWNSTAIRS( '>', "there are stairs going down"),
    UPSTAIRS(   '<', "there are stairs going up"),
    CLOSED_DOOR('+', "there is a closed door"),
    OPEN_DOOR(  '-', "there is an open door"),
    WINDOW(     '=', "there is a window"),

    FURNITURE('O', "there is a piece of furniture"),
    PORTAL(   '0', "there is an open path"),

    ENTITY(  'C', "there is a living entity"),
    ITEM(    '^', "there lies a collectable item"),
    TREASURE('$', "there lies a pile of treasure"),

    UNKNOWN('?', "there is something unknown");

    private char defaultSprite;
    private String description;
    private TileDefinition metadata;

    TileType(char defaultSprite, String description) {
        this.defaultSprite = defaultSprite;
        this.description = description;
        Constants.TILE_TYPE_MAP.put(defaultSprite, this);
    }

    public static TileType valueOf(Character character) {
        return Constants.TILE_TYPE_MAP.get(character);
    }

    private interface Constants {
        Map<Character, TileType> TILE_TYPE_MAP = new HashMap<>();
    }
    public char getSprite() {
        return defaultSprite;
    }

    public void setDefaultSprite(char defaultSprite) {
        this.defaultSprite = defaultSprite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TileDefinition getMetadata() {
        return metadata;
    }

    public void setMetadata(TileDefinition metadata) {
        this.metadata = metadata;
    }

    public boolean isDoor() {
        return this == OPEN_DOOR || this == CLOSED_DOOR;
    }

    public boolean isStairs() {
        return this == UPSTAIRS || this == DOWNSTAIRS;
    }
}
