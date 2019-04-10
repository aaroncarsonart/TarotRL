package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.imbroglio.Position2D;

/**
 * Defines the boundaries for a three-dimensional
 * bounded rectangle.
 */
public class Region2D {
    public final Position2D position;
    public final Position2D dimensions;

    public Region2D(Position2D position, Position2D dimensions) {
        this.position = position;
        this.dimensions = dimensions;
    }

    public Region2D(Position2D position, int dx, int dy) {
        this.position = position;
        this.dimensions = new Position2D(dx, dy);
    }

    public Region2D(int px, int py, Position2D dimensions) {
        this.position = new Position2D(px, py);
        this.dimensions = dimensions;
    }

    public Region2D(int px, int py, int dx, int dy) {
        this.position = new Position2D(px, py);
        this.dimensions = new Position2D(dx, dy);
    }
}
