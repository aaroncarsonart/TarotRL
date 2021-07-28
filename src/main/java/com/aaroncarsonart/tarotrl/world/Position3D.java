package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.map.Direction2D;
import com.aaroncarsonart.tarotrl.util.TriIntSupplier;
import com.aaroncarsonart.tarotrl.util.TriIntVisitor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The base class for all three dimensional game modeling.
 * Essentially a 3 dimensional origin structure.
 */
public class Position3D implements Comparable<Position3D> {

    public static final Position3D ORIGIN = new Position3D(0, 0, 0);

    public final int x;
    public final int y;
    public final int z;

    public Position3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position3D(Position3D p) {
        this(p.x, p.y, p.z);
    }

    public Position2D to2D() {
        return new Position2D(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position3D) {
            Position3D that = (Position3D) obj;
            return this.x == that.x && this.y == that.y && this.z == that.z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    public Position3D add(Position3D v) {
        int nx = this.x + v.x;
        int ny = this.y + v.y;
        int nz = this.z + v.z;
        return new Position3D(nx, ny, nz);
    }

    public Position3D subtract(Position3D p) {
        int nx = this.x - p.x;
        int ny = this.y - p.y;
        int nz = this.z - p.z;
        return new Position3D(nx, ny, nz);
    }

    public Position3D multiply(Position3D p) {
        int nx = this.x * p.x;
        int ny = this.y * p.y;
        int nz = this.z * p.z;
        return new Position3D(nx, ny, nz);
    }

    public Position3D divide(Position3D p) {
        int nx = this.x / p.x;
        int ny = this.y / p.y;
        int nz = this.z / p.z;
        return new Position3D(nx, ny, nz);
    }

    public double distance(Position3D p1) {
        Position3D p2 = this;
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
    }

    /**
     * Considering Position3D objects as voxels, are the coordinates
     * of this origin contained within the described offset grid?
     */
    public boolean isWithinVoxelGridOf(Position3D voxelGridDimensions, Position3D voxelGridOrigin) {

        boolean withinX = voxelGridOrigin.x <= x && x < (voxelGridOrigin.x + voxelGridDimensions.x);
        boolean withinY = voxelGridOrigin.y <= y && y < (voxelGridOrigin.y + voxelGridDimensions.y);
        boolean withinZ = voxelGridOrigin.z <= z && z < (voxelGridOrigin.z + voxelGridDimensions.z);

        return withinX && withinY && withinZ;
    }

    public boolean isWithinVoxelGridOf_2(Position3D voxelGridDimensions, Position3D voxelGridOrigin) {

        Position3D translated = this.subtract(voxelGridOrigin);

        boolean withinX = 0 <= translated.x && translated.x < voxelGridDimensions.x;
        boolean withinY = 0 <= translated.y && translated.y < voxelGridDimensions.y;
        boolean withinZ = 0 <= translated.z && translated.z < voxelGridDimensions.z;

        return withinX && withinY && withinZ;
    }

    public boolean isWithinVoxelGridOf_3(Position3D voxelGridDimensions, Position3D voxelGridOrigin) {

        int minX = voxelGridOrigin.x;
        int maxX = voxelGridOrigin.x + voxelGridDimensions.x;

        int minY = voxelGridOrigin.y;
        int maxY = voxelGridOrigin.y + voxelGridDimensions.y;

        int minZ = voxelGridOrigin.z;
        int maxZ = voxelGridOrigin.z + voxelGridDimensions.z;

        boolean withinX = minX <= x && x < maxX;
        boolean withinY = minY <= x && x < maxY;
        boolean withinZ = minZ <= x && x < maxZ;

        return withinX && withinY && withinZ;
    }


    /*
    For each of x, y, and z:

    Is this.x within the bounds of the offset grids minX and maxX?

     */

    public Position3D withX(int nx) {
        return new Position3D(nx, y, z);
    }

    public Position3D withY(int ny) {
        return new Position3D(x, ny, z);
    }

    public Position3D withZ(int nz) {
        return new Position3D(x, y, nz);
    }

    public Position3D withRelativeX(int x) {
        int nx = this.x + x;
        return new Position3D(nx, y, z);
    }

    public Position3D withRelativeY(int y) {
        int ny = this.y + y;
        return new Position3D(x, ny, z);
    }

    public Position3D withRelativeZ(int z) {
        int nz = this.z + z;
        return new Position3D(x, y, nz);
    }

    public Position3D moveTowards(Direction3D direction) {
        if (direction == null) {
            return this;
        }
        return this.add(direction.offset);
    }

    public Position3D moveTowards(Direction2D direction) {
        return moveTowards(direction.getDirection3D());
    }

    public static Position3D from(int[] v) {
        int x = v.length > 0 ? v[0] : 0;
        int y = v.length > 1 ? v[1] : 0;
        int z = v.length > 2 ? v[2] : 0;
        return new Position3D(x, y, z);
    }

    @Override
    public String toString(){
        return toSuccinctString();
    }

    public String toVerboseString(){
        return "Position3D(x=" + x + ", y=" + y + ", z=" + z + ")";
    }

    public String toSuccinctString(){
        return "(" + x + "," + y + "," + z + ")";
    }

    /**
     * Create a 3-dimensional array of of the given DIMENSIONS and type.
     * @param dimensions
     * @param supplier
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][][] createGrid3D(Position3D start, Position3D dimensions, TriIntSupplier<T> supplier) {
        T instance = supplier.supply(0, 0, 0);
        T[][][] grid = (T[][][]) Array.newInstance(instance.getClass(), dimensions.x, dimensions.y, dimensions.z);
        for (int x = 0; x < dimensions.x; x++) {
            for (int y = 0; y < dimensions.y; y++) {
                for (int z = 0; z < dimensions.z; z++) {
                    int vx = start.x + x;
                    int vy = start.y + y;
                    int vz = start.z + z;
                    grid[x][y][z] = supplier.supply(vx, vy, vz);
                }
            }
        }
        return grid;
    }

    public static void forEach(Position3D start, Position3D max, TriIntVisitor visitor) {
        for (int x = start.x; x < max.x; x++) {
            for (int y = start.y; y < max.y; y++) {
                for (int z = start.z; z < max.z; z++) {
                    visitor.visit(x, y, z);
                }
            }
        }
    }

    public static void forEach(Position3D start, Position3D max, Consumer<Position3D> visitor) {
        for (int x = start.x; x < max.x; x++) {
            for (int y = start.y; y < max.y; y++) {
                for (int z = start.z; z < max.z; z++) {
                    Position3D next = new Position3D(x, y, z);
                    visitor.accept(next);
                }
            }
        }
    }

    public static List<Position3D> range(Region3D region) {
        Position3D start = region.position;
        Position3D max = region.dimensions.add(region.dimensions);
        return range(start, max);
    }

    public static List<Position3D> range(Position3D start, Position3D max) {
        LinkedList<Position3D> list = new LinkedList<>();
        Position3D.forEach(start, max, list::add);
        return list;
    }

    @Override
    public int compareTo(@NotNull Position3D that) {
        int xCompare = Integer.compare(this.x, that.x);
        if (xCompare != 0) {
            return xCompare;
        }
        int yCompare = Integer.compare(this.y, that.y);
        if (yCompare != 0) {
            return yCompare;
        }
        int zCompare = Integer.compare(this.z, that.z);
        return zCompare;
    }
}
