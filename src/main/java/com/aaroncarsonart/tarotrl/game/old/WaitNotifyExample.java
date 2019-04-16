package com.aaroncarsonart.tarotrl.game.old;

import com.aaroncarsonart.tarotrl.graphics.GameColors;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEvent;
import org.hexworks.zircon.api.uievent.KeyboardEventType;
import org.hexworks.zircon.api.uievent.UIEventResponse;

import java.util.Stack;

/**
 * Here's a little proof of concept movement design
 */
public class WaitNotifyExample {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE;

        boolean pressed = false;
        boolean consumed = false;

        Direction getOpposite() {
            switch(this) {
                case UP:    return DOWN;
                case DOWN:  return UP;
                case LEFT:  return RIGHT;
                case RIGHT: return LEFT;
            }
            return NONE;
        }
    }

    TileGrid tileGrid;
    Position current;
    Tile bgTile;
    Tile moveTile;

    Object lock = new Object();

    Stack<Direction> moves = new Stack<>();
    boolean singleStep = false;

    private void init() {

        bgTile = Tiles.newBuilder()
                .withBackgroundColor(GameColors.BLACK)
                .withCharacter(' ')
                .build();

        moveTile = Tiles.newBuilder()
                .withBackgroundColor(GameColors.MAGENTA)
                .withCharacter(' ')
                .build();

        current = Positions.create(0, 0);

        tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
                .withTitle("WaitNotifyExample")
                .withSize(20, 20)
                .build());

        tileGrid.setTileAt(current, moveTile);

        tileGrid.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> onKeyPressed(event));
        tileGrid.onKeyboardEvent(KeyboardEventType.KEY_RELEASED, (event, phase) -> onKeyReleased(event));

        final int moveSpeedMillis = 100;

        // game loop
        while (true) {
            try {
                if (singleStep || moves.isEmpty()) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                update();
                if (!singleStep) {
                    Thread.sleep(moveSpeedMillis);
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    private UIEventResponse onKeyPressed(KeyboardEvent event) {
        synchronized (moves) {
            boolean updated = false;

            Direction direction = getDirection2D(event);
            if (direction != Direction.NONE && !direction.pressed && !direction.consumed) {
                direction.pressed = true;
                if (!moves.contains(direction)) {
                    updated = true;
                    moves.push(direction);
                }
            }

            if (event.getCode() == KeyCode.SHIFT) {
                updated = true;
                singleStep = true;
            }

            if (updated) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }

        return UIEventResponses.processed();
    }

    private UIEventResponse onKeyReleased(KeyboardEvent event) {
        synchronized (moves) {

            boolean updated = false;

            Direction direction = getDirection2D(event);
            if (direction != Direction.NONE) {
                direction.pressed = false;
                direction.consumed = false;
                if (moves.contains(direction)) {
                    updated = true;
                    moves.remove(direction);
                }
            }

            if (event.getCode() == KeyCode.SHIFT) {
                updated = true;
                singleStep = false;
            }

            if (updated) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }

        }

        return UIEventResponses.processed();
    }

    int updates = 0;

    public void update() {
        System.out.println("update " + ++updates);
        // NavigationComponent
        if (!moves.isEmpty()) {
            Direction direction = moves.peek();
            if (moves.contains(direction.getOpposite())) {
                direction = Direction.NONE;
            }
            if (singleStep) {
                moves.remove(direction);
            }
            moveTile(direction);
        }
    }

    private void moveTile(Direction direction) {
        // NavigationComponent
        direction.consumed = true;
        Position next = move(current, direction);

        System.out.println(next);

        // GraphicsComponent
        tileGrid.setTileAt(current, bgTile);
        tileGrid.setTileAt(next, moveTile);
        current = next;
    }


    private Direction getDirection2D(KeyboardEvent event) {
        switch (event.getCode()) {
            case UP:    return Direction.UP;
            case DOWN:  return Direction.DOWN;
            case LEFT:  return Direction.LEFT;
            case RIGHT: return Direction.RIGHT;
            default:    return Direction.NONE;
        }
    }

    private Position move(Position position, Direction direction) {
        Position next;
        switch (direction) {
            case UP:    next = position.withRelativeY(-1); break;
            case DOWN:  next = position.withRelativeY(1);  break;
            case LEFT:  next = position.withRelativeX(-1); break;
            case RIGHT: next = position.withRelativeX(1);  break;
            default:    next = position;
        }
        if (tileGrid.getSize().containsPosition(next)) {
            return next;
        } else {
            return position;
        }
    }

    public static void main(String[] args) {
        WaitNotifyExample game = new WaitNotifyExample();
        game.init();
    }
}
