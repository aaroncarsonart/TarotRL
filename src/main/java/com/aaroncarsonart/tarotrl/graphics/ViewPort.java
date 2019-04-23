package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.map.Region2D;
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

    public ViewPort(Region2D region) {
        this.x = region.position.x();
        this.y = region.position.y();
        this.width = region.dimensions.x();
        this.height = region.dimensions.y();
    }
}
