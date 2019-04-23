package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.map.Region2D;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.TextAlignment;

/**
 * Represents a message with a prompt of options, such as:
 * <pre>
 *     Are you sure you want to quit?
 *     Yes            No
 * </pre>
 */
public class OptionMessage extends Menu {

    private Message message;
    private ListMenu options;

    public OptionMessage(String message, int maxLength, MenuItem ... options) {
        this.options = new ListMenu();
        this.options.setMenuLayout(MenuLayout.HORIZONTAL);
        for(MenuItem option : options) {
            this.options.addMenuItem(option);
        }
        this.message = new Message(message, maxLength, null);
    }

    public OptionMessage(String message, MenuItem ... options) {
        this(message, message.length(), options);
    }

    public OptionMessage(Message message, ListMenu options) {
        this.message = message;
        this.options = options;
        this.options.setMenuLayout(MenuLayout.HORIZONTAL);
    }

    private Callback calculateCancelCallback() {
        Callback cancel = super.getCancelCallback();
        if (cancel != null) {
            return cancel;
        } else if (!options.getMenuItems().isEmpty()) {
            MenuItem lastOption = options.getMenuItems().get(options.getMenuItems().size() -1);
            return lastOption::execute;
        }
        return () -> {};
    }

    @Override
    public void update(MenuAction menuAction) {
        if (menuAction == MenuAction.CANCEL) {
            Callback cancel = calculateCancelCallback();
            cancel.execute();
        } else {
            options.update(menuAction);
        }
    }


    public void addOption(String label, Callback optionCallback) {
        options.addMenuItem(new MenuItem(label, optionCallback));
    }

    public ListMenu getOptions() {
        return options;
    }

    public Message getMessage() {
        return message;
    }

    public int getContentWidth() {
        int messageWidth = message.getContentWidth();
        int optionsWidth = options.getContentWidth();

        if (messageWidth <= optionsWidth) {
            return optionsWidth;
        } else {
            // Recalculate messageWidth and optionsWidth to be congruent.
            int n = options.getMenuItems().size();
            int optionLength = (int) Math.ceil(messageWidth / (double) n);
            int calcWidth = optionLength * n;
            message.setMaxLength(calcWidth);
            options.setMaxItemLength(optionLength);
            return calcWidth;
        }
    }

    public int getContentHeight() {
        return message.getContentHeight() + options.getContentHeight();
    }

    @Override
    public Region2D getContentRegion(int x, int y) {
        int width = getContentWidth();
        int height = getContentHeight();
        return new Region2D(x, y, width, height);
    }

    @Override
    public void setTextAlignment(TextAlignment textAlignment) {
        super.setTextAlignment(textAlignment);
        message.setTextAlignment(textAlignment);
        options.setTextAlignment(textAlignment);
    }

    @Override
    public void resetCursor() {
        message.resetCursor();
        options.resetCursor();
    }
}
