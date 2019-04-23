package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.menu.InventoryMenuData;
import com.aaroncarsonart.tarotrl.menu.Menu;
import com.aaroncarsonart.tarotrl.menu.MenuAction;
import com.aaroncarsonart.tarotrl.util.Logger;

/**
 * Manage game state for: {@link com.aaroncarsonart.tarotrl.game.GameMode#INVENTORY}.
 */
public class InventoryGameController implements GameController {
    private static final Logger LOG = new Logger(InventoryGameController.class);

    @Override
    public boolean update(GameState state, UserInput input) {
        Enum action = input.getAction();
        LOG.info(action);
        if (action instanceof MenuAction) {
            MenuAction menuAction = (MenuAction) action;
            InventoryMenuData menuData = state.getInventoryMenuData();
            Menu menu = menuData.getRootMenu();
            menu.update(menuAction);
            return true;
        }
        return false;
    }
}
