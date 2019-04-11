package com.aaroncarsonart.tarotrl.world;

/**
 * Defines the boundaries for a three-dimensional
 * bounded rectangle.
 */
public class Region3D {
    public final Position3D position;
    public final Position3D dimensions;

    public Region3D(Position3D position, Position3D dimensions) {
        this.position = position;
        this.dimensions = dimensions;
    }

    public Region3D(Position3D position, int dx, int dy, int dz) {
        this.position = position;
        this.dimensions = new Position3D(dx, dy, dz);
    }

    public Region3D(int px, int py, int pz, Position3D dimensions) {
        this.position = new Position3D(px, py, pz);
        this.dimensions = dimensions;
    }

    public Region3D(int px, int py, int pz, int dx, int dy, int dz) {
        this.position = new Position3D(px, py, pz);
        this.dimensions = new Position3D(dx, dy, dz);
    }

    public int volume() {
        return dimensions.x * dimensions.y * dimensions.z;
    }

    @Override
    public String toString() {
        return "Region3D:{ position" + position + ", dimensions" + dimensions + " }";
    }
}
