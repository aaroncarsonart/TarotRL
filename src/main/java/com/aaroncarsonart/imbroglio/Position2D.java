package com.aaroncarsonart.imbroglio;

import com.aaroncarsonart.tarotrl.world.Position3D;
import com.aaroncarsonart.tarotrl.map.Region2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

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
     * Constructor - creates a new origin variable at x/y.
     */
    public Position2D() {
        x = 0;
        y = 0;
    }

    /**
     * Non-default constructor - this takes an x and y variable to start the
     * origin.
     */
    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Non-default constructor - this takes an x and y variable to start the
     * origin.
     */
    public Position2D(Position2D newPos) {
        x = newPos.x();
        y = newPos.y();
    }

    public Position3D to3D(int z) {
        return new Position3D(x, y, z);
    }

    public Position3D to3D() {
        return new Position3D(x, y, 0);
    }

    @Override
    public int hashCode() {
        return HASH + (x * 10000) + y;
    }

    /**
     * Compare for content equality against another origin.
     * @param p The origin against which to compare.
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
     * Sets the x and y origin to the new values.
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x and y origin to new values held by the new origin.
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
     * moves the current origin left by 1.
     */
    public void moveLeft() {
        x -= 1;
    }

    /**
     * moves the current origin right by 1.
     */
    public void moveRight() {
        x += 1;
    }

    /**
     * moves the current origin up by 1.
     */
    public void moveUp() {
        y -= 1;
    }

    /**
     * moves the current origin down by 1.
     */
    public void moveDown() {
        y += 1;
    }

    /**
     * Move this origin in a random direction.
     */
    public void moveRandom() {
        int next = RANDOM.nextInt(4);
        move(next);
    }

    /**
     * Move this Position2D in one of the four cardinal directions according to the input value,
     * which is dependent on the remainder of absolute value of nextInt when divided by four. The
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
     * origin.
     *
     * @param checkPos The origin to check against.
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
     * origin.
     *
     * @param checkPos The origin to check against.
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
     * check origin.
     *
     * @param checkPos The origin to check against.
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
     * check origin.
     *
     * @param checkPos The origin to check against.
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
     * Convenience method to get every neighbor of this origin, i.e. get below(), right(),
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
     * Get a origin from the specified direction
     * @param direction A Direction2D enum, either NORTH, SOUTH, WEST, or EAST.
     *                  Returns null if an invalid direction was passed.
     * @return A new Position2D, oriented from the original Position2D by the input Direction2D.
     */
    public Position2D moveTowards(Direction direction) {
        switch (direction) {
            case UP:   return above();
            case DOWN: return below();
            case LEFT: return left();
            case RIGHT:return right();
            case NONE: return new Position2D(this);
            default: return null;
        }
    }

    /**
     * Move in the specified Direction2D.
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

    public Position2D add(Position2D v) {
        int nx = this.x + v.x;
        int ny = this.y + v.y;
        return new Position2D(nx, ny);
    }

    public Position2D subtract(Position2D p) {
        int nx = this.x - p.x;
        int ny = this.y - p.y;
        return new Position2D(nx, ny);
    }

    public Position2D multiply(Position2D p) {
        int nx = this.x * p.x;
        int ny = this.y * p.y;
        return new Position2D(nx, ny);
    }

    public Position2D divide(Position2D p) {
        int nx = this.x / p.x;
        int ny = this.y / p.y;
        return new Position2D(nx, ny);
    }

    public double distance(Position2D p1) {
        Position2D p2 = this;
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public static void forEach(Position2D start, Position2D max, Consumer<Position2D> visitor) {
        for (int x = start.x; x < max.x; x++) {
            for (int y = start.y; y < max.y; y++) {
                Position2D next = new Position2D(x, y);
                visitor.accept(next);
            }
        }
    }

    public static List<Position2D> range(Region2D region) {
        Position2D start = region.position;
        Position2D max = region.position.add(region.dimensions);
        return range(start, max);
    }

    public static List<Position2D> range(Position2D start, Position2D max) {
        LinkedList<Position2D> list = new LinkedList<>();
        Position2D.forEach(start, max, list::add);
        return list;
    }

    public static Position2D origin() {
        return new Position2D(0, 0);
    }

    @Override
    public String toString(){
        return toSuccinctString();
    }

    public String toVerboseString(){
        return "Position2D(x=" + x + ", y=" + y + ")";
    }

    public String toSuccinctString(){
        return "(" + x + "," + y + "," + ")";
    }



}
