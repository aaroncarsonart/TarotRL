package com.aaroncarsonart.tarotrl.util;

@FunctionalInterface
public interface TriIntSupplier<T> {
    T supply(int x, int y, int z);
}