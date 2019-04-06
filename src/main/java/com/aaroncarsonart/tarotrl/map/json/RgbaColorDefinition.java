package com.aaroncarsonart.tarotrl.map.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RgbaColorDefinition {

    private int r = 255;
    private int g = 255;
    private int b = 255;
    private int a = 255;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public TileColor toTileColor() {
        return TileColors.create(r, g, b, a);
    }
}
