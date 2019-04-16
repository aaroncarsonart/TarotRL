package com.aaroncarsonart.tarotrl.game.controller;

import com.aaroncarsonart.imbroglio.Position2D;
import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.input.GameAction;
import com.aaroncarsonart.tarotrl.input.UserInput;
import com.aaroncarsonart.tarotrl.util.Logger;

/**
 * This drives all game state behavior for the INVENTORY GameMode.
 */
public class InventoryGameController implements GameController {
    private static final Logger LOG = new Logger(InventoryGameController.class);

    @Override
    public boolean update(GameState state, UserInput input) {
        Enum action = input.getCurrentAction();
        LOG.info(action);
        if (action instanceof GameAction) {
            GameAction gameAction = (GameAction) action;
            switch (gameAction) {
//                case OK:
                case MOVE_UP:    return doMove(state,  0, -1);
                case MOVE_DOWN:  return doMove(state,  0,  1);
                case MOVE_LEFT:  return doMove(state,  1,  0);
                case MOVE_RIGHT: return doMove(state, -1,  0);
            }
        }

        return false;
    }

    private boolean doMove(GameState gameState, int vx, int vy) {
        InventoryGameControllerData data = gameState.getMapGameControllerData();
        Position2D current = data.getPosition();
        int nx = current.x() + vx;
        int ny = current.y() + vy;
        Position2D next = new Position2D(nx, ny);
        data.setPosition(next);
        LOG.info("do move from %s to %s", current, next);
        return true;
    }

}
