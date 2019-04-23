package com.aaroncarsonart.tarotrl.util;

@FunctionalInterface
public interface BiIntSupplier<T> {
    T supply(int x, int y);
}