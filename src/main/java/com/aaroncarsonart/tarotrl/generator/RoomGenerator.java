package com.aaroncarsonart.tarotrl.generator;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.util.RNG;

public class RoomGenerator {
    private static final char EMPTY = ' ';
    private static final char PATH = '.';
    private static final char WALL = '#';

    public Room generateRectangularRoom(int width, int height) {
        return new Room (width, height);
    }

    private Room getRandomSquareRoom(int minW, int maxW, int minH, int maxH) {
        int rw = RNG.nextInt(minW, maxW);
        int rh = RNG.nextInt(minH, maxH);
        return new Room (rw, rh);
    }


    public Room generateRoomOfRooms(int width, int height, int maxRooms, int minW, int maxW, int minH, int maxH) {
        Room map = new Room(width, height);
        map.fillWith(EMPTY);


        // create first room
        int rw = RNG.nextInt(minW, maxW);
        int rh = RNG.nextInt(minH, maxH);
        Room next = new Room (rw, rh);
        next.asWalledRoom();
        next.print();
        // place




        for (int i = 0; i < maxRooms; i++) {

            // create next room
            rw = RNG.nextInt(minW, maxW);
            rh = RNG.nextInt(minH, maxH);
            next = new Room (rw, rh);
            next.fillWith(PATH);

            // try to place

            Position2D target = map.getRandomPosition();

            //map.print();
        }



        return map;
    }


    public Room generate() {
        int width = 30;
        int height = 20;
        Room map = new Room(width, height);
        map.fillWith(EMPTY);



        Position2D p1 = map.getRandomPosition();
        Position2D p2 = map.getRandomPosition();
        Position2D[] bounds = asRoomBounds(p1, p2);









        return map;
    }

    /**
     * Take 2 positions, and produce an array of four elements representing
     * the corners of the box these positions represent, in the following order:
     * TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT.
     * @param p1 The first position to use.
     * @param p2 The second position to use.
     * @return An array of the four corners of the box these positions draw.
     */
    private Position2D[] asRoomBounds(Position2D p1, Position2D p2) {
        int minX = Math.min(p1.x(), p2.x());
        int maxX = Math.max(p1.x(), p2.x());
        int minY = Math.min(p1.y(), p2.y());
        int maxY = Math.max(p1.y(), p2.y());

        Position2D tl = new Position2D(minX, minY);
        Position2D tr = new Position2D(maxX, minY);
        Position2D bl = new Position2D(minX, maxY);
        Position2D br = new Position2D(maxX, maxY);

        return new Position2D[]{tl, tr, bl, br};
    }

    private void drawRoomBounds(Room room, Position2D[] bounds, char value) {
        Position2D tl = bounds[0];
        Position2D tr = bounds[1];
        Position2D bl = bounds[2];
        Position2D br = bounds[3];
        room.fill(tl, tr, value);
        room.fill(tl, bl, value);
        room.fill(tr, br, value);
        room.fill(bl, br, value);
    }



    public static void main(String[] args) {
        RoomGenerator g = new RoomGenerator();
        Room r = g.generateRoomOfRooms(30, 20, 5, 3, 10, 3, 10);
        r.print();
    }

}
