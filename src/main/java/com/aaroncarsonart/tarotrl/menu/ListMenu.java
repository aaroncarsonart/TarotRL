package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.map.Region2D;

import java.util.ArrayList;

/**
 * A Menu Implementation that is laid out as a single horizontal
 * or vertical list of MenuItems.
 */
public class ListMenu extends Menu {

    private String title = "";

    private int cursor = 0;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();

    private int maxItemLength = -1;
    private MenuLayout menuLayout = MenuLayout.VERTICAL;

    public ListMenu(){
    }

    public ListMenu(String title) {
        this.title = title;
    }

    @Override
    public void update(MenuAction menuAction) {
        switch (menuAction) {
            case UP:
            case LEFT:
            case PREVIOUS:
                previous();
                break;
            case DOWN:
            case RIGHT:
            case NEXT:
                next();
                break;
            case OK:
                MenuItem menuItem = menuItems.get(cursor);
                menuItem.execute();
                break;
            case CANCEL:
                cancel();
                break;
            // TODO: Figure out if Menus need to drive this functionality themselves?
        }
    }

    private void previous() {
        cursor -= 1;
        if (cursor == -1) {
            cursor = menuItems.size() - 1;
        }
    }

    private void next() {
        cursor += 1;
        if (cursor == menuItems.size()) {
            cursor = 0;
        }
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        this.maxItemLength = -1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MenuLayout getMenuLayout() {
        return menuLayout;
    }

    public void setMenuLayout(MenuLayout menuLayout) {
        this.menuLayout = menuLayout;
    }

    public int getCursor() {
        return cursor;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Region2D getContentRegion(int x, int y) {
        int width = getContentWidth();
        int height = getContentHeight();
        Region2D region = new Region2D(x, y, width, height);
        return region;
    }

    public int getMaxItemLength() {
        if (maxItemLength == -1) {
            for (MenuItem item: menuItems) {
                int itemLength = item.getLabel().length();
                maxItemLength = Math.max(maxItemLength, itemLength);
            }
            if (menuLayout == MenuLayout.HORIZONTAL) {
                maxItemLength += 1;
            }
        }
        return maxItemLength;
    }

    public void setMaxItemLength(int maxItemLength) {
        this.maxItemLength = maxItemLength;
    }

    public int getContentWidth() {
        int width;
        if (menuLayout == MenuLayout.VERTICAL) {
            width = getMaxItemLength();
        } else {
            width = menuItems.size() * getMaxItemLength();
//            width = menuItems.size() * getMaxItemLength() + Math.max(0, menuItems.size() - 1);
        }
        return width;
    }

    public int getContentHeight() {
        int height;
        if (menuLayout == MenuLayout.VERTICAL) {
            height = menuItems.size();
        } else {
            height = 1;
        }
        return height;
    }

    @Override
    public void resetCursor() {
        cursor = 0;
    }
}
