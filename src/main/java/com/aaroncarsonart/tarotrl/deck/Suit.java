package com.aaroncarsonart.tarotrl.deck;

import com.aaroncarsonart.tarotrl.util.JsonUtils;

public class Suit extends Keywordable {
    private int rank;
    private String name;
    private char sprite;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSprite() {
        return sprite;
    }

    public void setSprite(char sprite) {
        this.sprite = sprite;
    }

    @Override
    public String toString() {
        return JsonUtils.writeValueAsString(this);
    }
}
