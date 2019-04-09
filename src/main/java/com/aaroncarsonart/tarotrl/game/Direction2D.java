package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.imbroglio.Direction;
import com.aaroncarsonart.tarotrl.world.Direction3D;

public enum Direction2D {
    UP   ("\u2191", -1,  0),
    DOWN ("\u2193",  1,  0),
    LEFT ("\u2190",  0, -1),
    RIGHT("\u2192",  0,  1),
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
     * Gets the opposite of the input direction (for example: UP returns DOWN).
     * @param direction The direction to get the opposite of.
     * @return The opposite direction.
     */
    public static Direction2D getOpposite(Direction2D direction) {
        switch (direction) {
            case LEFT:  return RIGHT;
            case RIGHT: return LEFT;
            case UP:    return DOWN;
            case DOWN:  return UP;
            case NONE:  return NONE;
        }
        return null;
    }

    public String getInspectString() {
        switch (this) {
            case LEFT:  return "to the left";
            case RIGHT: return "to the right";
            case UP:    return "above";
            case DOWN:  return "below";
            default:    return "where you are standing,";
        }

    }

    public Direction getImbroglioDirection() {
        switch (this) {
            case LEFT:  return Direction.LEFT;
            case RIGHT: return Direction.RIGHT;
            case UP:    return Direction.UP;
            case DOWN:  return Direction.DOWN;
            case NONE:  return Direction.NONE;
        }
        return null;
    }

    public Direction3D getDirection3D() {
        switch (this) {
            case LEFT:  return Direction3D.WEST;
            case RIGHT: return Direction3D.EAST;
            case UP:    return Direction3D.NORTH;
            case DOWN:  return Direction3D.SOUTH;
        }
        return null;
    }
}