package com.aaroncarsonart.imbroglio;

 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Random;

/**
 * The class Position2D, used to hold positional data.
 *
 * @author Aaron Carson
 * @version 1.0
 */
public final class Position2D implements Serializable {

    // ************************************************************************
    // Fields
    // ************************************************************************
    protected static final Random RANDOM = new Random();
    private static final int HASH = 10001000;
    private int x;
    private int y;

    /**
     * Constructor - creates a new position variable at x/y.
     */
    public Position2D() {
        x = 0;
        y = 0;
    }

    /**
     * Non-default constructor - this takes an x and y variable to start the
     * position.
     */
    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Non-default constructor - this takes an x and y variable to start the
     * position.
     */
    public Position2D(Position2D newPos) {
        x = newPos.x();
        y = newPos.y();
    }

    @Override
    public int hashCode() {
        return HASH + (x * 10000) + y;
    }

    /**
     * Compare for content equality against another position.
     * @param p The position against which to compare.
     * @return True, if the Position2D has the same x and y values.
     */
    public boolean equals(Position2D p) {
        return p.x == this.x && p.y == this.y;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position2D) {
            Position2D p = (Position2D) obj;
            return p.x == this.x && p.y == this.y;
        }
        return false;
    }

    /**
     * Sets the x and y position to the new values.
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x and y position to new values held by the new position.
     * @param newPosition The Position2D to move this to.
     */
    public void set(Position2D newPosition) {
        x = newPosition.x();
        y = newPosition.y();
    }

    /**
     * Sets the x value to a new value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y value to a new value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * gets the value of x.
     */
    public int x() {
        return x;
    }

    /**
     * gets the value of y.
     */
    public int y() {
        return y;
    }

    /**
     * moves the current position left by 1.
     */
    public void moveLeft() {
        x -= 1;
    }

    /**
     * moves the current position right by 1.
     */
    public void moveRight() {
        x += 1;
    }

    /**
     * moves the current position up by 1.
     */
    public void moveUp() {
        y -= 1;
    }

    /**
     * moves the current position down by 1.
     */
    public void moveDown() {
        y += 1;
    }

    /**
     * Move this position in a random direction.
     */
    public void moveRandom() {
        int next = RANDOM.nextInt(4);
        move(next);
    }

    /**
     * Move this Position2D in one of the four cardinal directions according to the input value,
     * which is dependent on the remainder of absolute value of next when divided by four. The
     * associations are: (0 :: up, 1 :: down, 2 :: left, 3 :: right).
     * @param next The number to calculate the move from.
     */
    public void move(int next) {
        next = Math.abs(next) % 4;
        if (next == 0) {
            moveUp();
        } else if (next == 1) {
            moveDown();
        } else if (next == 2) {
            moveRight();
        } else if (next == 3) {
            moveLeft();
        }
    }

    /**
     * Returns true if this Position2D is adjacent and above the passed check
     * position.
     *
     * @param checkPos The position to check against.
     */
    public boolean isAdjacentAbove(Position2D checkPos) {
        if (this.x == checkPos.x && (this.y - 1) == checkPos.y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this Position2D is adjacent and below the passed check
     * position.
     *
     * @param checkPos The position to check against.
     */
    public boolean isAdjacentBelow(Position2D checkPos) {
        if (this.x == checkPos.x && (this.y + 1) == checkPos.y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this Position2D is adjacent and to the left the passed
     * check position.
     *
     * @param checkPos The position to check against.
     */
    public boolean isAdjacentLeft(Position2D checkPos) {
        if ((this.x - 1) == checkPos.x && this.y == checkPos.y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this Position2D is adjacent and to the right of the passed
     * check position.
     *
     * @param checkPos The position to check against.
     */
    public boolean isAdjacentRight(Position2D checkPos) {
        if ((this.x + 1) == checkPos.x && this.y == checkPos.y) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a new Position2D that is exactly above this one (y - 1)
     * @return A new Position2D.
     */
    public Position2D above() {
        return new Position2D(x, y - 1);
    }

    /**
     * Get a new Position2D that is exactly below this one (y + 1)
     * @return A new Position2D.
     */
    public Position2D below() {
        return new Position2D(x, y + 1);
    }

    /**
     * Get a new Position2D that is exactly to the left of this one (x - 1)
     * @return A new Position2D.
     */
    public Position2D left() {
        return new Position2D(x - 1, y);
    }

    /**
     * Get a new Position2D that is exactly to the right of this one (x + 1)
     * @return A new Position2D.
     */
    public Position2D right() {
        return new Position2D(x + 1, y);
    }


    /**
     * Convenience method to get every neighbor of this position, i.e. get below(), right(),
     * above(), and left(), called in that order.
     * @return An ArrayList of Positions.
     */
    public ArrayList<Position2D> getNeighbors(){
        ArrayList<Position2D> neighbors = new ArrayList<Position2D>();
        neighbors.add(this.below());
        neighbors.add(this.right());
        neighbors.add(this.above());
        neighbors.add(this.left());
        return neighbors;
    }



    /**
     * Get a position from the specified direction
     * @param direction A Direction enum, either UP, DOWN, LEFT, or RIGHT.
     *                  Returns null if an invalid direction was passed.
     * @return A new Position2D, oriented from the original Position2D by the input Direction.
     */
    public Position2D moveTowards(Direction direction) {
        switch (direction) {
            case UP:   return above();
            case DOWN: return below();
            case LEFT: return left();
            case RIGHT:return right();
            default: return null;
        }
    }

    /**
     * Move in the specified Direction.
     * @param direction
     */
    public void move(Direction direction){
        switch (direction) {
            case UP:    moveUp();
                break;
            case DOWN:  moveDown();
                break;
            case LEFT:  moveLeft();
                break;
            case RIGHT: moveRight();
                break;
        }

    }


    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }

}