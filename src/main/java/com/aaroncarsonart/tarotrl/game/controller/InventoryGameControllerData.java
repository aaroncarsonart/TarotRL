package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.imbroglio.Position2D;

public class InventoryGameControllerData {

    private Position2D position; // = new Position2D(0, 0);


    public InventoryGameControllerData() {
        position = new Position2D(5, 5);
    }

    public Position2D getPosition() {
        return position;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }
}
