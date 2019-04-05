package com.aaroncarsonart.tarotrl.game.zircon;

import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;

/**
 * Encapsulate the specific colors used to render GameMaps.
 */
public class GameColors {

    public static final TileColor BLACK = TileColors.create(0, 0, 0);
    public static final TileColor WHITE = TileColors.create(255, 255, 255);

    public static final TileColor RED   = TileColors.create(255, 0, 0);
    public static final TileColor GREEN = TileColors.create(0, 255, 0);
    public static final TileColor BLUE  = TileColors.create(0, 0, 255);

    public static final TileColor YELLOW  = TileColors.create(255, 255, 0);
    public static final TileColor CYAN    = TileColors.create(0, 255, 255);
    public static final TileColor MAGENTA = TileColors.create(255, 0, 255);

    public TileColor bgWall;
    public TileColor fgWall;
    public TileColor bgOpen;
    public TileColor fgOpen;

    public TileColor player;

    public TileColor bgEmpty;
    public TileColor fgEmpty;

    public static final GameColors FOREST_GREEN = new GameColors() {{
        bgWall = TileColors.create(0, 0, 0);
        fgWall = TileColors.create(0, 25, 0);

        bgOpen = TileColors.create(0, 50, 0);
        fgOpen = TileColors.create(0, 200, 0);

        player = TileColors.create(255, 255, 0);

        bgEmpty = TileColors.create(0, 0, 0);
        fgEmpty = TileColors.create(160, 160, 160);
    }};
}
