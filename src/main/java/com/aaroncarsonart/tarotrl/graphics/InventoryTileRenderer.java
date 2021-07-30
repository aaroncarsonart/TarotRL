package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.Region2D;
import com.aaroncarsonart.tarotrl.menu.InventoryMenuData;
import com.aaroncarsonart.tarotrl.menu.ListMenu;
import com.aaroncarsonart.tarotrl.menu.Menu;
import com.aaroncarsonart.tarotrl.menu.MenuItem;
import com.aaroncarsonart.tarotrl.menu.MenuLayout;
import com.aaroncarsonart.tarotrl.menu.Message;
import com.aaroncarsonart.tarotrl.menu.OptionMessage;
import com.aaroncarsonart.tarotrl.util.Logger;
import com.aaroncarsonart.tarotrl.util.TextAlignment;
import com.aaroncarsonart.tarotrl.util.TextUtils;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.grid.TileGrid;

import java.util.List;

/**
 * Manage tile rendering for: {@link com.aaroncarsonart.tarotrl.game.GameMode#INVENTORY}.
 */
public class InventoryTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(InventoryTileRenderer.class);

    private TileColor fgColor;
    private TileColor bgColor;
    private TileColor selectedColor;

    public InventoryTileRenderer() {
        fgColor = GameColors.WHITE;
        bgColor = GameColors.BLACK;
        selectedColor = GameColors.CYAN;
    }

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        clearTileData(tileGrid);
        InventoryMenuData menuData = gameState.getInventoryMenuData();
        Menu menu = menuData.getRootMenu();
        Position position = Positions.defaultPosition();
        if (menu instanceof OptionMessage) {
            OptionMessage message = (OptionMessage) menu;
            renderOptionMessage(message, tileGrid, position);
        } else if (menu instanceof ListMenu) {
            ListMenu listMenu = (ListMenu) menu;
            renderListMenu(listMenu, tileGrid, position);
        } else if (menu instanceof Message) {
            Message message = (Message) menu;
            renderMessage(message, tileGrid, position);
        }
    }

    private void renderMessage(Message message, TileGrid tileGrid, Position position) {
        if (message.isDrawBorder()) {
            renderMenuBorder(message, tileGrid, position);
            position = position.plus(Positions.create(1, 1));
        }
        int maxLength = message.getMaxLength();
        TextAlignment alignment = message.getTextAlignment();
        for (String line : message.getFormattedText()) {
            String formattedLine = TextUtils.align(line, maxLength, alignment);
            writeText(tileGrid, formattedLine, position.getX(), position.getY(), fgColor, bgColor);
            position = position.withRelativeY(1);
        }
    }

    private void renderOptionMessage(OptionMessage optionMessage, TileGrid tileGrid, Position position) {
        if (optionMessage.isDrawBorder()) {
            renderMenuBorder(optionMessage, tileGrid, position);
            position = position.plus(Positions.create(1, 1));
        }
        // write message
        Message message = optionMessage.getMessage();
        renderMessage(message, tileGrid, position);
        position = position.withRelativeY(message.getContentHeight());

        // write options
        ListMenu options = optionMessage.getOptions();
        renderHorizontalListMenu(options, tileGrid, position);
    }

    private void renderMenuBorder(Menu menu, TileGrid tileGrid, Position position) {
        Region2D region = menu.getContentRegion(position.getX(), position.getY());
        int width = region.dimensions.x() + 2;
        int height = region.dimensions.y() + 2;
        ViewPort viewPort = new ViewPort(region.position.x(), region.position.y(), width, height);
        drawSimpleBorder(tileGrid, viewPort, false);
    }

    private void renderListMenu(ListMenu menu, TileGrid tileGrid, Position position) {
        if (menu.isDrawBorder()) {
            renderMenuBorder(menu, tileGrid, position);
            position = position.plus(Positions.create(1, 1));
        }
        if (menu.getMenuLayout() == MenuLayout.VERTICAL) {
            renderVerticalListMenu(menu, tileGrid, position);
        } else {
            renderHorizontalListMenu(menu, tileGrid, position);
        }
    }

    private void renderVerticalListMenu(ListMenu listMenu, TileGrid tileGrid, Position position) {
        // Draw each menu item, inversing the colors for the item at the cursor position.
        int cursor = listMenu.getCursor();
        int maxItemLength = listMenu.getMaxItemLength();
        TextAlignment alignment = listMenu.getTextAlignment();
        List<MenuItem> menuItems = listMenu.getMenuItems();
        for(int i = 0; i < menuItems.size(); i++) {
            TileColor fg = fgColor;
            TileColor bg = bgColor;

            MenuItem menuItem = menuItems.get(i);
            if (menuItem.isSelected()) {
                fg = selectedColor;
            }
            // Swap colors for the cursor position.
            if (i == cursor) {
                TileColor fgTmp = fg;
                fg = bg;
                bg = fgTmp;
            }
            int x = position.getX();
            int y = position.getY() + i;

            String label = menuItem.getLabel();
            String formattedLabel = TextUtils.align(label, maxItemLength, alignment);
            writeText(tileGrid, formattedLabel, x, y, fg, bg);
        }
    }

    private void renderHorizontalListMenu(ListMenu listMenu, TileGrid tileGrid, Position position) {
        // Draw each menu item, inversing the colors for the item at the cursor position.
        int cursor = listMenu.getCursor();
        int maxItemLength = listMenu.getMaxItemLength();
        TextAlignment alignment = listMenu.getTextAlignment();
        List<MenuItem> menuItems = listMenu.getMenuItems();
        for(int i = 0; i < menuItems.size(); i++) {
            TileColor fg = fgColor;
            TileColor bg = bgColor;
            // Swap colors for the cursor position.
            if (i == cursor) {
                fg = bgColor;
                bg = fgColor;
            }
            int x = position.getX() + i * (maxItemLength);
            int y = position.getY();

            MenuItem menuItem = menuItems.get(i);
            String label = menuItem.getLabel();
            String formattedLabel = TextUtils.align(label, maxItemLength, alignment);
            writeText(tileGrid, formattedLabel, x, y, fg, bg);
        }
    }
}
