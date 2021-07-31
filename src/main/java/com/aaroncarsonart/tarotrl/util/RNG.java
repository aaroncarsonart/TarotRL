package com.aaroncarsonart.tarotrl.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RNG {

    private static final Random rng = Globals.RANDOM;

//    /**
//     * Fetch a random position that lies within the bounds of the specified region.
//     * @param bounds
//     * @return
//     */
//    public static Position2D randomPosition(Region2D bounds) {
//        int x = r.nextInt(bounds.dimensions.x());
//    }

//    private <T> T removeElement(List<T> list) {
//        int i = rng.nextInt(list.size());
//        return list
//    }

    public static <T> void shuffle(List<T> list) {
        Collections.shuffle(list, rng);
    }

    public static boolean nextBoolean() {
        return rng.nextBoolean();
    }

    public static int nextInt() {
        return rng.nextInt();
    }

    public static int nextInt(int bound) {
        return rng.nextInt(bound);
    }

    public static int nextInt(int min, int max) {
        return min + rng.nextInt(max - min);
    }

    public static double nextDouble(double min, double max) {
        return min + rng.nextDouble() * (max - min);
    }
}
