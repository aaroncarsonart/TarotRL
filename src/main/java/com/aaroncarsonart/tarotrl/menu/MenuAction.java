package com.aaroncarsonart.tarotrl.menu;

/**
 * All MenuActions that must be supported by a given Menu.
 */
public enum MenuAction {
    /** Select and execute the given MenuItem's callback.*/
    OK,
    /** Cancel the current menu, and navigate back one previous Menu.*/
    CANCEL,
    /** Navigate the Menu cursor to the logical "next" menu item.*/
    NEXT,
    /** Navigate the Menu cursor to the logical "previous" menu item.*/
    PREVIOUS,
    /** Navigate to the relative position that is "above" the cursor.*/
    UP,
    /** Navigate to the relative position that is "below" the cursor.*/
    DOWN,
    /** Navigate to the relative position that is "to the left" of the cursor.*/
    LEFT,
    /** Navigate to the relative position that is "to the right" of the cursor.*/
    RIGHT,
}
