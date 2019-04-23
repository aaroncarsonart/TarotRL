package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.input.InputController;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.util.Logger;

/**
 * The Game class links all components together and manages the game loop.
 * <p>
 * Proper use of the Game class requires four steps:
 * <ol>
 *     <li>Set the game's starting state: {@link #setGameState(GameState)}</li>
 *     <li>Create and register game modes: {@link #registerGameMode(GameMode, GameModeComponents)}/li>
 *     <li>Configure the graphics context: {@link #setGraphicsContext(GraphicsContext)}</li>
 *     <li>Begin the Game: {@link #start()}</li>
 * </ol>
 */
public class Game {
    private static final Logger LOG = new Logger(Game.class);

    private GameState gameState;
    private GameModeManager gameModeManager;
    private InputController inputController;
    private GraphicsContext graphicsContext;

    private Object lock;
    private int updateCount;

    public Game() {
        this.gameModeManager = new GameModeManager();
    }

    /**
     * Set the initial GameState.
     * @param gameState The GameState to set.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Register a GameMode.
     * @param gameMode The GameMode enum to register.
     * @param components The the input, controller, and renderer for this GameMode.
     */
    public void registerGameMode(GameMode gameMode, GameModeComponents components) {
        this.gameModeManager.registerComponents(gameMode, components);
    }

    /**
     * The configured GraphicsContext to use for this Game.
     * @param graphicsContext The GraphicsContext to configure.
     */
    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    /**
     * Run the game!
     *
     * Before running the game, the GameState and GraphicsContext must be set,
     * and at least one GameMode must be registered.
     */
    public void start() {
        graphicsContext.registerTileRenderers(gameModeManager);
        graphicsContext.initTileGrid();

        inputController = new InputController();
        inputController.registerInputHandlers(gameModeManager);
        inputController.listenForInputs(graphicsContext.getUIEventSource(), gameState);

        lock = inputController.acquireInputLock();

        graphicsContext.render(gameState);

        gameLoop();
        System.exit(0);
    }

    /**
     * Separate out the main game loop control logic into a separate method.
     */
    private void gameLoop() {
        LOG.info("begin game loop!");
        // game loop
        boolean running = true;
        while (running) {
            try {
//                if (singleStep || moves.isEmpty()) {
                    synchronized (lock) {
                        lock.wait();
                    }
//                }
//            }
                update();
                running = !gameState.isGameOver();
//                if (!singleStep) {
//                    Thread.sleep(moveSpeedMillis);
//                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Execute one complete update of the Game's state.
     */
    private void update() {
        boolean updated = false;
        UserInput userInput = inputController.getUserInput();
        LOG.info("update %d", updateCount++);

        GameMode gameMode = gameState.getActiveGameMode();
        GameController gameController = gameModeManager.getGameController(gameMode);
        updated |= gameController.update(gameState, userInput);
        if (updated) {
            graphicsContext.render(gameState);
        }
    }
}
