package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.util.Callback;

public class YesNoMessage {

    private String question;
    private ListMenu menu;

    public YesNoMessage(String question, Callback yesCallback, Callback noCallback) {
        this.question = question;
        menu = new ListMenu();
        menu.addMenuItem(new MenuItem("Yes", yesCallback));
        menu.addMenuItem(new MenuItem("No", noCallback));
    }

    public void update(MenuAction menuAction) {

    }
}
