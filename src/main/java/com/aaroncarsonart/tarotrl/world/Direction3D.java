package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.aaroncarsonart.tarotrl.util.Logger;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the six cardinal directions that a Position3D
 * can move within a three-dimensional coordinate space.
 */
public enum Direction3D {

    WEST(Position3D.ORIGIN.withRelativeX(-1)),
    EAST(Position3D.ORIGIN.withRelativeX(1)),
    NORTH(Position3D.ORIGIN.withRelativeY(-1)),
    SOUTH(Position3D.ORIGIN.withRelativeY(1)),
    ABOVE(Position3D.ORIGIN.withRelativeZ(1)),
    BELOW(Position3D.ORIGIN.withRelativeZ(-1));

    public final Position3D offset;

    Direction3D(Position3D voxel) {
        this.offset = voxel;
    }

    /**
     * Representing the horizontal plane, which the world map is explored by.
     */
    public static final Set<Direction3D> XY_PLANE = EnumSet.of(WEST, EAST, NORTH, SOUTH);
    private static final Logger LOG = new Logger(Direction3D.class);

    public Set<Direction3D> getOrthogonalDirections() {
        switch (this) {
            case WEST:
            case EAST:
                return EnumSet.of(NORTH, SOUTH, ABOVE, BELOW);
            case NORTH:
            case SOUTH:
                return EnumSet.of(WEST, EAST, ABOVE, BELOW);
            case ABOVE:
            case BELOW:
                return EnumSet.of(WEST, EAST, NORTH, SOUTH);
            default:
                String message = "Unhandled Direction3D: " + this;
                LOG.error(message);
                throw new TarotRLException(message);
        }
    }

    public Direction3D getOpposite() {
        switch(this) {
            case WEST: return EAST;
            case EAST: return WEST;
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case ABOVE: return BELOW;
            case BELOW: return ABOVE;
            default:
                String message = "Unhandled Direction3D: " + this;
                LOG.error(message);
                throw new TarotRLException(message);
        }
    }
}
