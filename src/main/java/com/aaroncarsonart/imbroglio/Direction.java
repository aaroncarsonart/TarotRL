package com.aaroncarsonart.imbroglio;

/**
 * Direction2D enum for abstracting directional input.
 */
public enum Direction {
    LEFT       ("\u2190"),
    UP         ("\u2191"),
    RIGHT      ("\u2192"),
    DOWN       ("\u2193"),
    HORIZONTAL ("\u2194"),
    VERTICAL   ("\u2195"),
    NONE       (" ");

    private String	unicode;

    /**
     * Create a new Direction2D with the specified arrow character.
     *
     * @param unicode The unicode character string to represent this Direction2D.
     */
    Direction(String unicode) {
        this.unicode = unicode;
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
            case LEFT:       return RIGHT;
            case RIGHT:      return LEFT;
            case UP:         return DOWN;
            case DOWN:       return UP;
            case HORIZONTAL: return VERTICAL;
            case VERTICAL:   return HORIZONTAL;
            default:         return NONE;
        }
    }

}
