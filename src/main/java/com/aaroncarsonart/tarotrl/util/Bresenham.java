package com.aaroncarsonart.tarotrl.util;

import com.aaroncarsonart.imbroglio.Position2D;

import java.util.ArrayList;
import java.util.List;

public class Bresenham {
    /**
     * Algorithm taken directly from: https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @return A list of positions representing a line plotted between the coordinates.
     */
    public static List<Position2D> plotLine(int x1, int y1, int x2, int y2) {
        List<Position2D> plot = new ArrayList<>();
        // delta of exact value and rounded value of the dependent variable
        int d = 0;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;

        if (dx >= dy) {
            while (true) {
                plot.add(new Position2D(x, y));
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                plot.add(new Position2D(x, y));
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
        return plot;
    }
}
