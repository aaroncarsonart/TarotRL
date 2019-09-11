package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.util.Callback;

public class MenuItem {

    private String label;
    private String description;
    private Callback callback;
    private boolean selected;

    public MenuItem(String label, String description, Callback callback) {
        this.label = label;
        this.description = description;
        this.callback = callback;
    }

    public MenuItem(String label, Callback callback) {
        this.label = label;
        this.description = "";
        this.callback = callback;
    }

    public MenuItem(String label) {
        this.label = label;
        this.description = "";
        this.callback = () -> selected = !selected;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public void execute() {
        callback.execute();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
