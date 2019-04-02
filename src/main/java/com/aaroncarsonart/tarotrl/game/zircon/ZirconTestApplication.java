package com.aaroncarsonart.tarotrl.game.zircon;

import com.aaroncarsonart.imbroglio.Maze;
import com.aaroncarsonart.tarotrl.game.Direction;
import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.grid.TileGrid;

/**
 * Just a rough first implementation.  To be deleted once the proper game
 * proceeds into more robust development, and this scratch code becomes
 * completely obsolete.
 */
public class ZirconTestApplication {

    public static void testMazeDisplayText() {
        System.out.println("Hello, world!");
        int width = 31;
        int height = 31;

        Maze maze = Maze.generateRandomWalledMaze(width, height);

        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(width, height))
                        .withDefaultTileset(CP437TilesetResources.mdCurses16x16())
                        .build());
        System.out.println("Hello, world, after TileGrid initialization.");

        tileGrid.onKeyStroke(keyStroke -> {
            System.out.println("onKeyStroke(): " + keyStroke.toString());
        });

        tileGrid.onKeyPressed(' ', keyStroke -> {
            Direction d = null;
            switch (keyStroke.inputType()) {
                case ArrowUp:
                    d = Direction.UP;
                    break;
                case ArrowDown:
                    d = Direction.DOWN;
                    break;
                case ArrowLeft:
                    d = Direction.LEFT;
                    break;
                case ArrowRight:
                    d = Direction.RIGHT;
                    break;
                default:
                    d = Direction.NONE;
                    break;
            }
            System.out.println("onKeyPressed(): " + keyStroke.toString());
            if (d != null) {
                System.out.println(d);
            }
        });

        TileColor black = TileColors.create(0, 0, 0);
        TileColor white = TileColors.create(255, 255, 255);

        TileColor red = TileColors.create(255, 0, 0);
        TileColor green = TileColors.create(0, 255, 0);
        TileColor blue = TileColors.create(0, 0, 255);

        TileColor yellow = TileColors.create(255, 255, 0);
        TileColor cyan = TileColors.create(0, 255, 255);
        TileColor magenta = TileColors.create(255, 0, 255);

        TileColor wallColorBG = TileColors.create(0, 0, 0);
        TileColor wallColorFG = TileColors.create(0, 50, 0);
        TileColor pathColorBG = TileColors.create(0, 50, 0);
        TileColor pathColorFG = TileColors.create(0, 200, 0);
        TileColor defaultColorBG = TileColors.create(0, 0, 0);
        TileColor defaultColorFG = TileColors.create(160, 160, 160);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                byte cell = maze.getCell(x, y);
                TileColor colorBG = cell == Maze.WALL ? wallColorBG : cell == Maze.PATH ? pathColorBG : defaultColorBG;
                TileColor colorFG = cell == Maze.WALL ? wallColorFG : cell == Maze.PATH ? pathColorFG : defaultColorFG;
                char spriteCharacter = cell == Maze.WALL ? '#' : cell == Maze.PATH ? '.' : ' ';

                tileGrid.setTileAt(
                        Positions.create(x, y),
                        Tiles.newBuilder()
                                .withBackgroundColor(colorBG)
                                .withForegroundColor(colorFG)
                                .withCharacter(spriteCharacter)
                                .build());
            }
        }
    }

    public static void testMazeDisplayColor() {
        int width = 31;
        int height = 31;

        Maze maze = Maze.generateRandomWalledMaze(width, height);

        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(width, height))
                        .withDefaultTileset(CP437TilesetResources.aduDhabi16x16())
                        .build());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                byte cell = maze.getCell(x, y);
                ANSITileColor bgColor = cell == Maze.PATH ? ANSITileColor.BRIGHT_GREEN : ANSITileColor.GREEN;
                ANSITileColor fgColor = cell == Maze.PATH ? ANSITileColor.BRIGHT_GREEN : ANSITileColor.GREEN;
                char spriteCharacter = cell == Maze.PATH ? '.' : cell == Maze.WALL ? '#' : ' ';

                tileGrid.setTileAt(
                        Positions.create(x, y),
                        Tiles.newBuilder()
                                .withBackgroundColor(bgColor)
                                .withForegroundColor(fgColor)
                                //.withCharacter(spriteCharacter)
                                .build());
            }
        }
    }


    public static void zirconTutorialTestCode() {
        TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withSize(Sizes.create(10, 10))
                        .withDefaultTileset(CP437TilesetResources.rexPaint16x16())
                        .build());

        tileGrid.setTileAt(
                Positions.create(2, 3),
                Tiles.newBuilder()
                        .withBackgroundColor(ANSITileColor.CYAN)
                        .withForegroundColor(ANSITileColor.WHITE)
                        .withCharacter('x')
                        .build());

        tileGrid.setTileAt(
                Positions.create(3, 4),
                Tiles.newBuilder()
                        .withBackgroundColor(ANSITileColor.RED)
                        .withForegroundColor(ANSITileColor.GREEN)
                        .withCharacter('y')
                        .build());

        tileGrid.setTileAt(
                Positions.create(4, 5),
                Tiles.newBuilder()
                        .withBackgroundColor(ANSITileColor.BLUE)
                        .withForegroundColor(ANSITileColor.MAGENTA)
                        .withCharacter('z')
                        .build());

    }


    public static void main(String[] args) {
//        Application application = SwingApplications.startApplication();
//        TileGrid tileGrid = SwingApplications.startTileGrid();

        testMazeDisplayText();
//        testMazeDisplayColor();
    }
}
