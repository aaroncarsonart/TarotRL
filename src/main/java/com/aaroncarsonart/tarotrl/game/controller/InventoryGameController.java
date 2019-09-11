package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.inventory.GameItem;
import com.aaroncarsonart.tarotrl.menu.InventoryMenuData;
import com.aaroncarsonart.tarotrl.menu.Menu;
import com.aaroncarsonart.tarotrl.menu.MenuAction;
import com.aaroncarsonart.tarotrl.menu.MenuItem;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.Logger;

import java.util.List;
import java.util.stream.Collectors;

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
            List<GameItem> playerInventory = state.getPlayerItems();
            List<MenuItem> menuItems = playerInventory.stream()
                    .map(item -> {
                        MenuItem menuItem = new MenuItem(item.getName(), item.getDescription(), null);
                        Callback callback = () -> menuItem.setSelected(!menuItem.isSelected());
                        menuItem.setCallback(callback);
                        return menuItem;
                    }).collect(Collectors.toList());
            if (menuItems.isEmpty()) {
                menuItems.add(new MenuItem("-"));
            }
            menuData.setItemMenu(menuItems);
            Menu menu = menuData.getRootMenu();
            menu.update(menuAction);
            return true;
        }
        return false;
    }
}
