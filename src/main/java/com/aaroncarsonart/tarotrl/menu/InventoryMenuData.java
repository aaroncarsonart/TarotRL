package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.TextAlignment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryMenuData {

    private Menu rootMenu;
    private Callback cancelAction;

    private ListMenu mainMenu;
    private ListMenu monsterMenu;
    private ListMenu itemMenu;
    private Menu someMessage;
    private OptionMessage quitMessage;
    private boolean resetCursor;

    public ListMenu buildListMenu(List<MenuItem> items, int start, int maxLength) {
        ListMenu listMenu = new ListMenu();
        for(int i = start; i < items.size() && i < (start + maxLength); i++) {
            MenuItem item = items.get(i);
            listMenu.addMenuItem(item);
        }
        return listMenu;
    }

    public InventoryMenuData() {
        Callback toMainMenu = () -> rootMenu = mainMenu;

        mainMenu = new ListMenu();
        mainMenu.addMenuItem(new MenuItem("Items", () -> rootMenu = itemMenu));
        mainMenu.addMenuItem(new MenuItem("Bestiary", () -> rootMenu = monsterMenu));
        mainMenu.addMenuItem(new MenuItem("???", () -> rootMenu = someMessage));
        mainMenu.addMenuItem(new MenuItem("Quit", () -> rootMenu = quitMessage));
        mainMenu.setMenuLayout(MenuLayout.HORIZONTAL);
        mainMenu.setTextAlignment(TextAlignment.LEFT);
        mainMenu.onCancel(() -> cancelAction.execute());
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

//        someMessage = new Message("What a horrible night for a roguelike!", toMainMenu);
//        OptionMessage message = new OptionMessage("What a horrible night for a roguelike!");
//        message.addOption("Ok", toMainMenu);
//        message.setDrawBorder(true);
//        someMessage = message;
        Message message = new Message("What a horrible night for a roguelike!", 15, toMainMenu);
        message.setDrawBorder(true);
        message.setTextAlignment(TextAlignment.CENTER);
        someMessage = message;

        quitMessage = new OptionMessage("Are you sure you want to quit?", 15);
        quitMessage.addOption("Yes", () -> System.exit(0));
        quitMessage.addOption("No", toMainMenu);
        quitMessage.setDrawBorder(true);
        quitMessage.setTextAlignment(TextAlignment.CENTER);

    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void setCancelAction(Callback cancelAction) {
        this.cancelAction = cancelAction;
    }

    public void setItemMenu(List<MenuItem> playerInventory) {
        itemMenu = buildListMenu(playerInventory, 0, playerInventory.size());
        itemMenu.setMenuLayout(MenuLayout.VERTICAL);
        itemMenu.setTextAlignment(TextAlignment.LEFT);
        itemMenu.onCancel(() -> rootMenu = mainMenu);
        itemMenu.setDrawBorder(true);

    }

}
