package com.aaroncarsonart.imbroglio;

/**
 * A Quad is an axis-aligned bounding box, represented by a center point and The
 * half distance to the left and to the right.
 * <p>
 * For consistency and practical Terms, Quads values should be measured and
 * recorded on a pixel scale. The position is a litera position, not related
 * directly to any tile sizes.
 *
 * @author Aaron Carson
 * @version Jun 30, 2015
 */
public final class Quad
{
    /** The center x coordinate position. */
    public double	x;
    /** The center y coordinate position. */
    public double	y;
    /** The half-width of the Quad. */
    public double	halfWidth;
    /** The half-height of the Quad. */
    public double	halfHeight;

    /**
     * Make a Quad.
     *
     * @param x The center x position.
     * @param y The center y Position2D.
     * @param halfWidth The distance to the left and right edges.
     * @param halfHeight The distance to the top and bottom edges.
     */
    public Quad(double x, double y, double halfWidth, double halfHeight) {
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    /**
     * Duplicate the given quad into a new quad with the same values.
     * @param quad The Quad to duplicate.
     */
    public Quad(Quad quad){
        this.x = quad.x;
        this.y = quad.y;
        this.halfWidth = quad.halfWidth;
        this.halfHeight = quad.halfHeight;
    }

    /**
     * Check if this collides with the given Quad, and shift it back the minimum
     * distance if so.
     *
     * @param quad The quad to compare to.
     * @return True, if this quad was adjusted.
     */
    public final boolean fixIfCollides(Quad quad) {

        // the minimum distances the two Quads can be from each other.
        double xMin = this.halfWidth + quad.halfWidth;
        double yMin = this.halfHeight + quad.halfHeight;

        // if positive, quad is to the right; if negative, it is to the left.
        double xDist = quad.x - this.x;
        // if positive, quad is below; if negative, it is above.
        double yDist = quad.y - this.y;

        // a collision occurs! resolve below
        double xAbs = Math.abs(xDist);
        double yAbs = Math.abs(yDist);
        if (xAbs < xMin && yAbs < yMin) {
            // adjust the greater distance first.
            if (xAbs > yAbs) {
                // adjust based on relative position.
                if (this.x < quad.x) {
                    this.x = quad.x - quad.halfWidth - this.halfWidth;
                    // System.out.println("collides!");
                }
                else {
                    this.x = quad.x + quad.halfWidth + this.halfWidth;
                    // System.out.println("collides!");
                }
            }
            else {
                // adjust based on relative position.
                if (this.y < quad.y) {
                    this.y = quad.y - quad.halfHeight - this.halfHeight;
                    // System.out.println("collides!");
                }
                else {
                    this.y = quad.y + quad.halfHeight + this.halfHeight;
                    // System.out.println("collides!");
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if the rectangle is off screen of the bounds of a map of given
     * width and height.
     *
     * @param width The width of the map to check.
     * @param height The height of the map to check.
     * @return True fi the screen is off screen.
     */
    public boolean offScreen(int width, int height) {
        return halfWidth > x || x >= width - halfWidth || halfHeight > y
                || y >= height - halfHeight;
    }

    /**
     * Get the full width of this Quad.
     */
    public double getWidth() {
        return halfWidth + halfWidth;
    }

    /**
     * Get the full height of this Quad.
     */
    public double getHeight() {
        return halfHeight + halfHeight;
    }

    public double getTopY() {
        return y - halfHeight;
    }

    public double getBottomY() {
        return y + halfHeight;
    }

    public double getLeftX() {
        return x - halfWidth;
    }

    public double getRightX() {
        return x + halfWidth;
    }

    /**
     * Get the orientation of this quad in relation to the given quad. If the
     * Quads overlap, then the default
     *
     * @param quad The quad to get the orientation from.
     * @return Direction.UP, DOWN, LEFT, or RIGHT, based on if this Quad is
     *         above, velow, or to the left or right respectively.
     */
    public Orientation getOrientationFrom(Quad quad) {
        // minimum distances
        double xMin = this.halfWidth + quad.halfWidth;
        double yMin = this.halfHeight + quad.halfHeight;

        // relative displacement
        double xDisplacement = this.x - quad.x;
        double yDisplacement = this.y - quad.y;

        // distances from center to center.
        double xCenterDistance = Math.abs(xDisplacement);
        double yCenterDistance = Math.abs(yDisplacement);

        // distances from edge to edge in x and y (min value of zero)
        double xDistance = Math.max(0, xCenterDistance - xMin);
        double yDistance = Math.max(0, yCenterDistance - yMin);

        // if distance is negative, then above/left
        // if positive then down/right
        boolean xIntersects = xDistance == 0;
        boolean yIntersects = yDistance == 0;

        // cases: UP - xIntersects and yDisplacement is positive
        // cases: DOWN - xIntersects and yDisplacement is negative or zero
        if (xIntersects) {
            if (yDisplacement > 0) return Orientation.ABOVE;
            else return Orientation.BELOW;
        }
        // cases: LEFT - yIntersects and xDisplacement is positive
        // cases: RIGHT - yIntersects and xDisplacement is negative or zero
        if (yIntersects) {
            if (xDisplacement > 0) return Orientation.TO_THE_LEFT;
            else return Orientation.TO_THE_LEFT;
        }

        // if intersects, just use DOWN (should never happen)
        return Orientation.BELOW;
    }

    /**
     * Get the minimum distance between this Quad and another Quad.
     *
     * @param quad The other Quad.
     * @return The smallest distance between two Quads.
     */
    public double getDistanceBetween(Quad quad) {
        // minimum distances
        double xMin = this.halfWidth + quad.halfWidth;
        double yMin = this.halfHeight + quad.halfHeight;

        // distances from center to center.
        double xCenterDistance = Math.abs(this.x - quad.x);
        double yCenterDistance = Math.abs(this.y - quad.y);

        // distances from edge to edge in x and y (min value of zero)
        double xDistance = Math.max(0, xCenterDistance - xMin);
        double yDistance = Math.max(0, yCenterDistance - yMin);

        // if distance is negative, then above/left
        // if positive then down/right
        boolean xIntersects = xDistance == 0;
        boolean yIntersects = yDistance == 0;

        // cases:
        if (xIntersects && yIntersects) return 0;
        else if (xIntersects) return yDistance;
        else if (yIntersects) return xDistance;
        else return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    /**
     * Check if the two Quads collide.
     *
     * @param quad The Quad to check against.
     * @return True, if they collide.
     */
    public boolean collides(Quad quad) {
        return Math.abs(this.x - quad.x) < this.halfWidth + quad.halfWidth
                && Math.abs(this.y - quad.y) < this.halfHeight
                + quad.halfHeight;
    }

    @Override
    public String toString() {
        return String.format("Quad: position: (%f, %f) dimensions: (%f, %f)",
                x, y, halfWidth * 2, halfHeight * 2);
    }
}
