package com.aaroncarsonart.tarotrl.util;

@FunctionalInterface
public interface TriIntVisitor {
    void visit(int x, int y, int z);
}