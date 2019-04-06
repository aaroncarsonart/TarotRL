package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.InputHandler;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.generator.GameMapGenerator;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Layers;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.TilesetResource;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The main class for encapsulating all game logic when using the Zircon UI.
 */
public class Game {

    public static GameState initGameState() {
        GameState gameState = new GameState();

        JsonDefinitionLoader loader = new JsonDefinitionLoader();
        GameMapGenerator generator = new GameMapGenerator();

//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/starting_vault.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/starting_maze.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/starting_random.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/starting_random_tunnels.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/starting_cellular_automata.json");

//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house_basement.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house_lv_1.json");
//        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house_lv_2.json");

        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house_lv_1.json");
        GameMap gameMap = generator.generateMapFrom(definition);

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        String initialStatus = gameMap.getName() + ".";
        gameState.setStatus(initialStatus);

        Position2D start = gameMap.findFirstOccurrence(TileType.PATH.getSprite());

        gameState.setPlayerPosition(start);

        return gameState;
    }

    public static void runZirconGame() {
        GameState gameState = initGameState();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

        // Hey, neat!  Some tilesets have graphics build in.  For example: aesomatica16x16
        TilesetResource tileSet = CP437TilesetResources.mdCurses16x16();

        int windowWidth = ((int) screenWidth)  / tileSet.getWidth();
        int windowHeight = ((int) screenHeight) / tileSet.getHeight();

        AppConfig appConfig = AppConfigs.newConfig()
                .withSize(Sizes.create(windowWidth, windowHeight))
                .withDefaultTileset(tileSet)
                .build();

        TileGrid tileGrid = SwingApplications.startTileGrid(appConfig);
        tileGrid.pushLayer(Layers.newBuilder().build());

        int xOffset = 20;
        int topOffSet = 1;
        int bottomOffSet = 10;
        Position mapOffset = Positions.create(xOffset, topOffSet);
        Size mapDimensions = Sizes.create(windowWidth - (xOffset * 2), windowHeight - (topOffSet + bottomOffSet));
        ViewPort mapViewPort = new ViewPort(mapOffset, mapDimensions);

        Layer mapLayer1 = new LayerBuilder()
                .withOffset(mapOffset)
                .withSize(mapDimensions)
                .build();

        tileGrid.pushLayer(mapLayer1);

//        Tile magentaMod = Tiles.newBuilder()
//                .withForegroundColor(GameColors.MAGENTA)
//                .withBackgroundColor(TileColors.transparent())
//                .withCharacter('%')
//                .build();
//
//        Tile blueOverlay = Tiles.newBuilder()
//                .withBackgroundColor(TileColors.create(0, 0, 255, 50))
//                .withCharacter(' ')
//                .build();
//
//        mapLayer1.setTileAt(Positions.create(0, 0), magentaMod);
//
//        Layer mapLayer2 = new LayerBuilder()
//                .withOffset(mapOffset.plus(Positions.create(5, 5)))
//                .withSize(mapDimensions.minus(Sizes.create(10, 10)))
//                .build()
//                .fill(blueOverlay);
//
//        tileGrid.pushLayer(mapLayer2);

        GameActionHandler actionHandler = new GameActionHandler();
        InputHandler inputHandler = new InputHandler();
        tileGrid.onKeyStroke(inputHandler::handleKeyStroke);

        TileRenderer tileRenderer = new TarotRenderer();
        tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
        tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);

        while (true) {
            PlayerAction nextAction = inputHandler.consumeNextAction();
            if (nextAction != PlayerAction.UNKNOWN) {
                actionHandler.processPlayerAction(nextAction, gameState);
                tileRenderer.renderGameMapThroughViewPort(tileGrid, gameState, mapViewPort);
                tileRenderer.drawGuiTextInfo(tileGrid, gameState, mapViewPort);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        runZirconGame();
    }
}
