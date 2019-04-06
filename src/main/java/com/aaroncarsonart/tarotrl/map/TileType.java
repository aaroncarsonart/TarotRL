package com.aaroncarsonart.tarotrl.map;

public enum TileType {
    PLAYER('@', "you have found yourself"),
    WALL(  '#', "there is a wall"),
    PATH(  '.', "there is an open path"),
    EMPTY( ' ', "there is nothing"),

    DOWNSTAIRS( '>', "there are stairs going up"),
    UPSTAIRS(   '<', "there are stairs going down"),
    CLOSED_DOOR('+', "there is a closed door"),
    OPEN_DOOR(  '-', "there is an open door"),
    WINDOW(     '=', "there is a window"),
    TREASURE(   '$', "there is treasure"),

    FURNITURE('O', "there is a piece of furniture"),
    PORTAL(   '0', "there is an open path"),

    UNKNOWN('?', "there is something unknown");

    private char defaultSprite;
    private String description;

    TileType(char defaultSprite, String description) {
        this.defaultSprite = defaultSprite;
        this.description = description;
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
}
