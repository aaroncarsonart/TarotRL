package com.aaroncarsonart.tarotrl.entity;

/**
 * Specifically for only moving about a GameWorld.
 * Paired with an MapEntity and a Direction.
 */
public enum MapNavigationAction {
    MOVE,
    STAIRS,
    DOOR,
    IDLE,
    TUNNEL
}
