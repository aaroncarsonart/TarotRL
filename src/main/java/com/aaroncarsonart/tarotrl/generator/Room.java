package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.RNG;

/**
 * Rooms are a definition of a portion of space of a map.
 * They are used for generation stages only, to create and
 * define an easily manipulatable shape definition
 */
public class Room {
    private static final Logger LOG = new Logger(Room.class);

    private static final char EMPTY = ' ';
    private static final char PATH = '.';
    private static final char WALL = '#';

    private int width;
    private int height;
    private char [][] cells;

    public Room (int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new char[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getCell(int x, int y) {
        return cells[y][x];
    }

    public char setCell(int x, int y) {
        return cells[y][x];
    }

    public boolean withinBounds(int x, int y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }

    public void fill(Position2D p1, Position2D p2, char value) {
        for(int y = p1.y(); y <= p2.y(); y++) {
            for (int x = p1.x(); x <= p2.x(); x++) {
                cells[y][x] = value;
            }
        }
    }

    public void fillWith(char value) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = value;
            }
        }
    }

//    public class RoomIterator {
//
//        Position2D current;
//
//        RoomIterator(Position2D start) {
//            current = new Position2D(start);
//        }
//
//        boolean has(Direction2D direction) {
//            Position2D next = current.moveTowards(direction);
//        }
//    }

    /**
     * Set the
     */
    public void asWalledRoom() {
        Position2D it = Position2D.origin();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = WALL;
            }
        }
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                cells[y][x] = PATH;
            }
        }
    }

    public Position2D getRandomPosition() {
        return new Position2D(RNG.nextInt(width), RNG.nextInt(height));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(width * height * 2);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char cell = cells[y][x];
                sb.append(cell);
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void print() {
        String header = "Room{width=" + width + ",height=" + height + "}";
        String cellData = toBorderedString();
        LOG.info("%s\n%s", header, cellData);
    }


    public String toBorderedString() {
        StringBuilder sb = new StringBuilder();

        sb.append('+');
        for (int x = 0; x < width; x++) {
            sb.append(" -");
        }
        sb.append(" +\n");

        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                sb.append(' ');
                sb.append(cells[y][x]);
            }
            sb.append(" |\n");
        }

        sb.append('+');
        for (int x = 0; x < width; x++) {
            sb.append(" -");
        }
        sb.append(" +\n");

        return sb.toString();
    }
}


