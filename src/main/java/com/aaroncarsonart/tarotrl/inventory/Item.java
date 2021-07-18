package com.aaroncarsonart.tarotrl.inventory;

import org.jetbrains.annotations.NotNull;

public class Item implements Comparable<Item> {

    private String name;
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(@NotNull Item o) {
        return this.name.compareTo(o.name);
    }
}
