package com.aaroncarsonart.tarotrl.menu;

import com.aaroncarsonart.tarotrl.map.Region2D;
import com.aaroncarsonart.tarotrl.util.Callback;
import com.aaroncarsonart.tarotrl.util.TextAlignment;

/**
 * The base Menu interface.
 */
public abstract class Menu {

    private Callback cancel;
    private boolean drawBorder;
    private TextAlignment textAlignment = TextAlignment.LEFT;

    public void onCancel(Callback cancelCallback) {
        this.cancel = cancelCallback;
    }

    public void cancel() {
        cancel.execute();
    }

    protected Callback getCancelCallback() {
        return cancel;
    }

    /**
     * Execute the given MenuAction on the Menu.
     * @param menuAction The MenuAction to execute.
     */
    public abstract void update(MenuAction menuAction);

    public TextAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public abstract Region2D getContentRegion(int x, int y);

    public boolean isDrawBorder() {
        return drawBorder;
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public abstract void resetCursor();

}
