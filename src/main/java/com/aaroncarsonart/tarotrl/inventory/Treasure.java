package com.aaroncarsonart.tarotrl.inventory;

import com.aaroncarsonart.tarotrl.util.RNG;

public class Treasure extends GameItem {

    private int amount;

    public Treasure (int amount) {
        super("a pile of treasure", "a pile of " + amount + " coins");
        this.amount = amount;
    }

    public Treasure() {
        this(initValue());
    }

    private static int initValue() {
        return 15 + RNG.nextInt(12) + RNG.nextInt(12);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
