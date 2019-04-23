package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.imbroglio.Position2D;

/**
 * A GridMenu is a generic two dimensional data structure of selectable objects.
 * This is entirely driven via text, so each menu item is driven by a displayed
 * label, an optional description  detailed text explaining the menu
 */
public class GridMenu {

    private int width;
    private int height;
    private Object[][] table;
    private Position2D cursor;

    /**
     * Create a GridMenu with the given capacity.
     * @param width The width of the table.
     * @param height The height of the table.
     */
    public GridMenu(int width, int height) {
        this.width = width;
        this.height = height;
        table = new Object[height][width];
    }

    public void addMenuItem(Position2D position, MenuItem menuItem) {

    }
}
