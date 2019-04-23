package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.map.Region2D;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.TextUtils;

import java.util.List;

/**
 * Represents a lightweight, single String message, to be displayed within a Menu system.
 */
public class Message extends Menu {

    private String text;
    private int maxLength;

    public Message(String text, Callback cancelAction) {
        this.text = text;
        this.maxLength = text.length();
        onCancel(cancelAction);
    }
    public Message(String text, int maxLength, Callback cancelAction) {
        this.text = text;
        this.maxLength = maxLength;
        onCancel(cancelAction);
    }

    public String getText() {
        return text;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> getFormattedText() {
        return TextUtils.getWordWrappedText(text, maxLength);
    }

    @Override
    public void update(MenuAction menuAction) {
        if (menuAction == MenuAction.OK || menuAction == MenuAction.CANCEL) {
            cancel();
        }
    }

    public int getContentWidth() {
        return maxLength;
    }

    public int getContentHeight() {
        int height;
            List<String> formattedText = getFormattedText();
            if (formattedText.size() == 1) {
                height = 1;
            } else {
                height = formattedText.size();
            }
        return height;
    }

    @Override
    public Region2D getContentRegion(int x, int y) {
        int width = getContentWidth();
        int height = getContentHeight();
        Region2D region = new Region2D(x, y, width, height);
        return region;
    }

    /**
     * Does nothing, a message has no cursor.
     */
    @Override
    public void resetCursor() {
    }
}
