package com.aaroncarsonart.tarotrl.input;

/**
 * Handle user input from Zircon API.  Convert into PlayerActions
 * or other commands that update specifically the GameState.
 *
 * Note: this only works with Zircon version 2018.12.25-XMAS.
 * for newer builds, use
 */
public class ZirconInputKeyStrokeHandler {
//
//    private static final Map<String, PlayerAction> inputActionsMap = initInputActionsMap();
//
//    private PlayerAction nextAction = PlayerAction.UNKNOWN;
//    private boolean devMode = false;
//    private boolean shiftDown = false;
//
//    private static Map<String, PlayerAction> initInputActionsMap() {
//        Map<String, PlayerAction> inputActionsMap = new LinkedHashMap<>();
//        inputActionsMap.put("?", PlayerAction.HELP);
//        inputActionsMap.put("h", PlayerAction.HELP);
//
//        inputActionsMap.put("q", PlayerAction.QUIT);
//
//        inputActionsMap.put("d", PlayerAction.DOOR);
//        inputActionsMap.put("p", PlayerAction.AUTO_PICKUP_ITEMS);
//
//        inputActionsMap.put("x", PlayerAction.CONFIRM);
//        inputActionsMap.put("c", PlayerAction.CANCEL);
//
//        inputActionsMap.put("up", PlayerAction.MOVE_UP);
//        inputActionsMap.put("down", PlayerAction.MOVE_DOWN);
//        inputActionsMap.put("left", PlayerAction.MOVE_LEFT);
//        inputActionsMap.put("right", PlayerAction.MOVE_RIGHT);
//
//        return inputActionsMap;
//    }
//
//    /**
//     * Compute the nextInt action to take, only if an existing action isn't
//     * already queued up waiting to be consumed.
//     * @param keyStroke The KeyStroke to be consumed.
//     */
//    public void handleKeyStroke(KeyStroke keyStroke, GameState gameState) {
//        System.out.println(keyStroke);
//        if (nextAction == PlayerAction.UNKNOWN) {
//            nextAction = computeNextAction(keyStroke);
//        }
//        gameState.setDevMode(this.getDevMove());
//        gameState.setShiftDown(this.getShiftDown());
//    }
//
//    private synchronized PlayerAction computeNextAction(KeyStroke keyStroke) {
//
//        this.devMode = keyStroke.isAltDown();
//        this.shiftDown = keyStroke.isShiftDown();
//
//        switch (keyStroke.inputType()) {
//            case ArrowUp:    return PlayerAction.MOVE_UP;
//            case ArrowDown:  return PlayerAction.MOVE_DOWN;
//            case ArrowLeft:  return PlayerAction.MOVE_LEFT;
//            case ArrowRight: return PlayerAction.MOVE_RIGHT;
//            case Space:      return PlayerAction.REST;
//            case Enter:  return PlayerAction.CONFIRM;
//            case Escape: return PlayerAction.CANCEL;
//        }
//        char key = keyStroke.getCharacter();
//        String keyStr = String.valueOf(key);
//        PlayerAction nextAction = inputActionsMap.getOrDefault(keyStr, PlayerAction.UNKNOWN);
//        return nextAction;
//    }
//
//    public synchronized PlayerAction consumeNextAction() {
//        PlayerAction consumedAction = nextAction;
//        nextAction = PlayerAction.UNKNOWN;
//        return consumedAction;
//    }
//
//    public boolean getDevMove() {
//        return devMode;
//    }
//
//    public boolean getShiftDown() {
//        return shiftDown;
//    }
}
