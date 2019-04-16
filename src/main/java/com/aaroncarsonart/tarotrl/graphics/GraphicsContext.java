package com.aaroncarsonart.tarotrl.graphics;

import com.aaroncarsonart.tarotrl.game.GameMode;
import com.aaroncarsonart.tarotrl.game.GameModeManager;
import com.aaroncarsonart.tarotrl.game.GameState;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.UIEventSource;

import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;

public class GraphicsContext extends Observable {

    private TileGrid tileGrid;
    private Map<GameMode, TileRenderer> tileRenderers = new EnumMap<>(GameMode.class);
    private Map<GameMode, Screen> screens = new EnumMap<>(GameMode.class);

    private String windowTitle;
    private Size windowDimensions;
    private TilesetResource tilesetResource;

    public void render(GameState gameState) {
        GameMode gameMode = gameState.getActiveGameMode();
        TileRenderer tileRenderer = tileRenderers.get(gameMode);
        Screen screen = screens.get(gameMode);
        tileRenderer.render(gameState, screen);
        screen.display();
        this.setChanged();
        this.notifyObservers(tileGrid);
    }

    public void registerTileRenderers(GameModeManager gameModeManager) {
        for (GameMode gameMode : gameModeManager.getRegisteredModes()) {
            TileRenderer tileRenderer = gameModeManager.getTileRenderer(gameMode);
            tileRenderers.put(gameMode, tileRenderer);
        }
    }

    public void initTileGrid() {
        // Begin Displaying TileGrid
        tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withTitle(windowTitle)
                .withSize(windowDimensions)
                .withDefaultTileset(tilesetResource)
                .build());

        for (GameMode gameMode : tileRenderers.keySet()) {
            Screen screen = Screens.createScreenFor(tileGrid);
            screens.put(gameMode, screen);
        }
    }


    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void setWindowDimensions(Size windowDimensions) {
        this.windowDimensions = windowDimensions;
    }

    public void setTilesetResource(TilesetResource tilesetResource) {
        this.tilesetResource = tilesetResource;
    }

    public UIEventSource getUIEventSource() {
        return tileGrid;
    }
}
