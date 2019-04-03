package com.aaroncarsonart.tarotrl.map;

public enum MapFeature {

    /**
     * Portals are any feature type that teleport the player from
     * one map and position to another map and position.  These may
     * be logically upstairs/downstairs, map boundaries, doors, etc.
     */
    PORTAL,

    /**
     * Doors are a feature that acts like a wall, but can be opened
     * to allow traffic between areas.  They may require a key to be
     * opened.
     */
    DOOR
}
