package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.game.controller.InventoryGameControllerData;
import com.aaroncarsonart.tarotrl.util.Logger;
import org.hexworks.zircon.api.DrawSurfaces;
import org.hexworks.zircon.api.Layers;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;

public class InventoryTileRenderer implements TileRenderer {
    private static final Logger LOG = new Logger(InventoryTileRenderer.class);

    private Screen screen;
    private Layer layer;
    private TileColor fgColor;
    private TileColor bgColor;

    private void init(TileGrid tileGrid) {
        fgColor = TileColors.create(255, 0, 0);
        bgColor = TileColors.create(255, 128, 0);

        Size layerSize = Sizes.create(6, 6);
        Position layerPosition = Positions.create(5, 5);
        layer = Layers.newBuilder()
                .withTileGraphics(DrawSurfaces.tileGraphicsBuilder()
                        .withSize(layerSize)
                        .build()
                        .fill(Tiles.newBuilder()
                                .withForegroundColor(fgColor)
                                .withBackgroundColor(bgColor)
                                .withCharacter('M')
                                .build()))
                .withOffset(layerPosition)
                .build();
        screen = Screens.createScreenFor(tileGrid);
        screen.pushLayer(layer);

    }

    @Override
    public void render(GameState gameState, TileGrid tileGrid) {
        if (screen == null) {
            init(tileGrid);
        }

        InventoryGameControllerData data = gameState.getMapGameControllerData();
        Position2D pos = data.getPosition();
        int x = pos.x();
        int y = pos.y();
        writeText(screen, "Hello, world!", 1, 1);

        GameMode mode = gameState.getActiveGameMode();
        writeText(screen, "I am currently in " + mode + " mode.", x + 1, y + 3, fgColor, bgColor);
        screen.display();
    }
}
