package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Direction;
import com.aaroncarsonart.tarotrl.world.Direction3D;

public enum Direction2D {
    NORTH("\u2191", -1,  0),
    SOUTH("\u2193",  1,  0),
    WEST("\u2190",  0, -1),
    EAST("\u2192",  0,  1),
    NONE (" "     ,  0,  0);

    private String unicode;
    private int dy;
    private int dx;

    Direction2D(String unicode, int dy, int dx) {
        this.unicode = unicode;
        this.dy = dy;
        this.dx = dx;
    }

    /**
     * @return The vertical positional offset for this direction.
     */
    public int getDy() {
        return dy;
    }

    /**
     * @return The horizontal positional offset for this direction.
     */
    public int getDx() {
        return dx;
    }

    @Override
    public String toString() {
        return unicode;
    }

    /**
     * Gets the opposite of the input direction (for example: NORTH returns SOUTH).
     * @param direction The direction to get the opposite of.
     * @return The opposite direction.
     */
    public static Direction2D getOpposite(Direction2D direction) {
        switch (direction) {
            case WEST:  return EAST;
            case EAST: return WEST;
            case NORTH:    return SOUTH;
            case SOUTH:  return NORTH;
            case NONE:  return NONE;
        }
        return null;
    }

    public String getInspectString() {
        switch (this) {
            case WEST:  return "to the left";
            case EAST: return "to the right";
            case NORTH:    return "above";
            case SOUTH:  return "below";
            default:    return "where you are standing,";
        }
    }

    public String getOrientationString() {
        Direction3D direction = getDirection3D();
        if (direction != null) {
            return direction.name().toLowerCase();
        }
        return null;
    }

    public Direction getImbroglioDirection() {
        switch (this) {
            case WEST:  return Direction.LEFT;
            case EAST: return Direction.RIGHT;
            case NORTH:    return Direction.UP;
            case SOUTH:  return Direction.DOWN;
            case NONE:  return Direction.NONE;
        }
        return null;
    }

    public Direction3D getDirection3D() {
        switch (this) {
            case WEST:  return Direction3D.WEST;
            case EAST: return Direction3D.EAST;
            case NORTH:    return Direction3D.NORTH;
            case SOUTH:  return Direction3D.SOUTH;
        }
        return null;
    }
}