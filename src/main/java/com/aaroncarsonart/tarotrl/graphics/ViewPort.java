package com.aaroncarsonart.tarotrl.graphics;

import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;

public class ViewPort {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public ViewPort(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ViewPort(Position offset, Size size) {
        this.x = offset.getX();
        this.y = offset.getY();
        this.width = size.getWidth();
        this.height = size.getHeight();
    }
}
