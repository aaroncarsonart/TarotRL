package com.aaroncarsonart.tarotrl.game;

public enum Direction {
    UP   ("\u2191", -1,  0),
    DOWN ("\u2193",  1,  0),
    LEFT ("\u2190",  0, -1),
    RIGHT("\u2192",  0,  1),
    NONE (" "     ,  0,  0);

    private String unicode;
    private int dy;
    private int dx;

    Direction(String unicode, int dy, int dx) {
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
    public static Direction getOpposite(Direction direction) {
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

    public com.aaroncarsonart.imbroglio.Direction getImbroglioDirection() {
        switch (this) {
            case LEFT:  return com.aaroncarsonart.imbroglio.Direction.LEFT;
            case RIGHT: return com.aaroncarsonart.imbroglio.Direction.RIGHT;
            case UP:    return com.aaroncarsonart.imbroglio.Direction.UP;
            case DOWN:  return com.aaroncarsonart.imbroglio.Direction.DOWN;
            case NONE:  return com.aaroncarsonart.imbroglio.Direction.NONE;
        }
        return null;
    }
}