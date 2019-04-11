package com.aaroncarsonart.tarotrl.world;

import com.aaroncarsonart.tarotrl.util.Logger;
import org.junit.jupiter.api.Test;

class Position3DTest {
    private static final Logger LOG = new Logger(Position3DTest.class);

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

        LOG.testing(point.isWithinVoxelGridOf(dimension, origin));
        LOG.testing(point.isWithinVoxelGridOf_2(dimension, origin));
    }
}