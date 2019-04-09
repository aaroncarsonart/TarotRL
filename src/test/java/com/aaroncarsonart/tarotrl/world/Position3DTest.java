package com.aaroncarsonart.tarotrl.world;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Position3DTest {

    @Test
    void testWithinVoxelGrid() {

//        Position3D origin = new Position3D(0, 0, 0);
//        Position3D origin = new Position3D(5, 5, 5);
//        Position3D dimension = new Position3D(5, 5, 5);
        Position3D origin = new Position3D(-5, 5, -5);
        Position3D dimension = new Position3D(5, 5, 5);

//        Position3D point = new Position3D(1, 1, 1);
//        Position3D point = new Position3D(6, 6, 6);
        Position3D point = new Position3D(-6, 6, -6);

        System.out.println(point.isWithinVoxelGridOf(dimension, origin));
        System.out.println(point.isWithinVoxelGridOf_2(dimension, origin));

    }

    @Test
    void testComparator() {
        List<Integer> myList = new ArrayList<>();
        myList.add(5);
        myList.add(1);
        myList.add(3);
        myList.add(7);
        myList.add(-1);
        myList.sort(Comparator.comparingInt(v -> v));
        System.out.println(myList);
    }

    @Test
    void testEnumOrdinal() {
        for(Direction3D direction: Direction3D.values()) {
            System.out.printf("%s: %d\n", direction.name(), direction.ordinal());
        }

    }

}