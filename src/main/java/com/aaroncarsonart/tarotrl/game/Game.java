package com.aaroncarsonart.tarotrl.game;

import com.aaroncarsonart.tarotrl.game.controller.GameController;
import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.input.InputController;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.util.Logger;

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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void registerGameMode(GameMode gameMode, GameModeComponents components) {
        this.gameModeManager.registerComponents(gameMode, components);
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

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

    private void update() {
        boolean updated = false;
        UserInput userInput = inputController.getUserInput();
//        do {
            LOG.info("update %d", updateCount++);
            gameState.setRepeatControllerUpdate(false);

            GameMode gameMode = gameState.getActiveGameMode();
            GameController gameController = gameModeManager.getGameController(gameMode);

            updated |= gameController.update(gameState, userInput);
            //userInput.retireCurrentAction();
//        } while (gameState.repeatControllerUpdate());
        if (updated) {
            graphicsContext.render(gameState);
        }

    }
}
