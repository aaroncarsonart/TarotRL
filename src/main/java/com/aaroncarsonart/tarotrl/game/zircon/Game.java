package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.PlayerAction;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameTile;
import com.aaroncarsonart.tarotrl.map.generator.GameMapGenerator;
import com.aaroncarsonart.tarotrl.map.json.GameMapDefinition;
import com.aaroncarsonart.tarotrl.map.json.JsonDefinitionLoader;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
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
        GameMapDefinition definition = loader.loadGameMapDefinition("/maps/vault_parents_house.json");

        GameMap gameMap = generator.generateMapFrom(definition);

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        Position2D start = gameMap.findFirstOccurrence(GameTile.PATH.getCharacter());

        gameState.setPlayerPosY(start.y());
        gameState.setPlayerPosX(start.x());

        return gameState;
    }

    public static void runZirconGame() {
        GameState gameState = initGameState();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;

       TilesetResource tileSet = CP437TilesetResources.rexPaint10x10();

        int windowWidth = ((int) screenWidth / 3 * 2)  / tileSet.getWidth();
        int windowHeight = ((int) screenHeight / 3 * 2) / tileSet.getHeight();


        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(windowWidth, windowHeight))
                        .withDefaultTileset(tileSet)
                        .build());

        Position mapOffset = Positions.create(20, 2);
        Size mapDimensions = Sizes.create(windowWidth - 40, windowHeight - 12);
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

        TileRenderer tileRenderer = new TileRenderer();
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
