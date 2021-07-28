package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.CardSelectionController;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.TextAlignment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryMenuData {

    private Menu rootMenu;
    private ListMenu mainMenu;
    private ListMenu monsterMenu;
    private ListMenu itemMenu;
    private OptionMessage quitMessage;

    public ListMenu buildListMenu(List<MenuItem> items, int start, int maxLength) {
        ListMenu listMenu = new ListMenu();
        for(int i = start; i < items.size() && i < (start + maxLength); i++) {
            MenuItem item = items.get(i);
            listMenu.addMenuItem(item);
        }
        return listMenu;
    }

    public InventoryMenuData(GameState state) {
        Callback toMainMenu = () -> rootMenu = mainMenu;

        mainMenu = new ListMenu();
        mainMenu.addMenuItem(new MenuItem("Items", () -> rootMenu = itemMenu));
        mainMenu.addMenuItem(new MenuItem("Bestiary", () -> rootMenu = monsterMenu));
        mainMenu.addMenuItem(new MenuItem("Deck", () -> loadTarotCardSelectionMenu(state)));
        mainMenu.addMenuItem(new MenuItem("Quit", () -> rootMenu = quitMessage));
        mainMenu.setMenuLayout(MenuLayout.HORIZONTAL);
        mainMenu.setTextAlignment(TextAlignment.LEFT);
        mainMenu.onCancel(() -> state.setGameMode(GameMode.MAP_NAVIGATION));
        mainMenu.setDrawBorder(true);
        rootMenu = mainMenu;

        List<MenuItem> inventoryItems = Arrays.asList("Herb","Herb","Torch","Scale","Emerald")
                .stream()
                .map(MenuItem::new)
                .collect(Collectors.toList());
        itemMenu = buildListMenu(inventoryItems, 1, 3);
        itemMenu.setMenuLayout(MenuLayout.VERTICAL);
        itemMenu.setTextAlignment(TextAlignment.LEFT);
        itemMenu.onCancel(toMainMenu);
        itemMenu.setDrawBorder(true);

        monsterMenu = new ListMenu();
        monsterMenu.addMenuItem(new MenuItem("Blue Slime"));
        monsterMenu.addMenuItem(new MenuItem("Red Slime"));
        monsterMenu.addMenuItem(new MenuItem("Drakee"));
        monsterMenu.addMenuItem(new MenuItem("Ghost"));
        monsterMenu.addMenuItem(new MenuItem("Skeleton"));
        monsterMenu.addMenuItem(new MenuItem("Werewolf"));
        monsterMenu.addMenuItem(new MenuItem("EVEN BIGGER???"));
        monsterMenu.setMenuLayout(MenuLayout.VERTICAL);
        monsterMenu.setTextAlignment(TextAlignment.CENTER);
        monsterMenu.onCancel(toMainMenu);
        monsterMenu.setDrawBorder(true);

        quitMessage = new OptionMessage("Are you sure you want to quit?", 15);
        quitMessage.addOption("Yes", () -> System.exit(0));
        quitMessage.addOption("No", toMainMenu);
        quitMessage.setDrawBorder(true);
        quitMessage.setTextAlignment(TextAlignment.CENTER);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void setItemMenu(List<MenuItem> playerInventory) {
        itemMenu = buildListMenu(playerInventory, 0, playerInventory.size());
        itemMenu.setMenuLayout(MenuLayout.VERTICAL);
        itemMenu.setTextAlignment(TextAlignment.LEFT);
        itemMenu.onCancel(() -> rootMenu = mainMenu);
        itemMenu.setDrawBorder(true);
    }

    public void loadTarotCardSelectionMenu(GameState state) {
        state.setGameMode(GameMode.CARD_SELECTION);
        CardSelectionController controller = (CardSelectionController) GameMode.CARD_SELECTION.getGameController();
        controller.setCancelCallback(() -> state.setGameMode(GameMode.INVENTORY));
        controller.setSelectCallback(controller::warpToGameMapUsingTarotCard);
        state.setSelectedCardIndex(0);
    }
}
