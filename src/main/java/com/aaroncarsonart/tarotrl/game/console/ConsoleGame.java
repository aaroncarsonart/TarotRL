package com.aaroncarsonart.tarotrl.game.console;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameActionHandler;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.map.GameMap;
import com.aaroncarsonart.tarotrl.map.GameMapUtils;

import java.util.LinkedList;

public class ConsoleGame {

//    public static final void testTarotRLCode() {
//        deck.TarotRL tarotRL = new deck.TarotRL();
//        tarotRL.init();
//        com.aaroncarsonart.tarotrl.map.GameMapUtils map = new com.aaroncarsonart.tarotrl.map.GameMapUtils(10, 10);
//        String mapStr = map.toString();
//        System.out.println(mapStr);
//    }

    public static GameState initGameState() {
        GameState gameState = new GameState();

        gameState.setPlayerPosition(new Position2D(1, 1));

//        Maze maze = Maze.generateRandomWalledMaze(21, 21);
//        GameMap gameMap = GameMapUtils.createGameMapFromMaze("Maze Map", maze);
        GameMap gameMap = GameMapUtils.readFileAsGameMap("/maps/test_game_map.txt");

        gameState.addGameMap(gameMap);
        gameState.setActiveMap(gameMap);

        return gameState;
    }

    public static void runConsoleGame() {
        ConsoleGameLoopHandler loopHandler = new ConsoleGameLoopHandler();
        ConsoleInputHandler inputHandler = new ConsoleInputHandler();
        GameActionHandler actionHandler = new GameActionHandler();

        LinkedList<GameState> gameStates = new LinkedList<>();
        gameStates.addFirst(initGameState());
        loopHandler.doDrawGameMapState(gameStates.peekFirst());

        while(true) {
            GameState previousGameState = gameStates.getLast();
            GameState activeGameState = previousGameState.createDeepCopy();

            loopHandler.doReadPlayerInput(inputHandler, actionHandler, activeGameState);
            loopHandler.doDrawGameMapState(activeGameState);

            gameStates.addLast(activeGameState);
        }

    }

    public static void main(String[] args) throws Exception {
        runConsoleGame();
        // testScanner();
    }
}
