package com.aaroncarsonart.tarotrl.game.zircon;

import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;

/**
 * Encapsulate the specific colors used to render GameMaps.
 */
public class ZirconGameMapColorProfile {

    TileColor wallBG;
    TileColor wallFG;
    TileColor pathBG;
    TileColor pathFG;

    TileColor player;

    TileColor defaultBG;
    TileColor defaultFG;

    public static final ZirconGameMapColorProfile FOREST_GREEN = new ZirconGameMapColorProfile() {{
        wallBG = TileColors.create(0, 0, 0);
        wallFG = TileColors.create(0, 50, 0);

        pathBG = TileColors.create(0, 50, 0);
        pathFG = TileColors.create(0, 200, 0);

        player = TileColors.create(255, 255, 0);

        defaultBG = TileColors.create(0, 0, 0);
        defaultFG = TileColors.create(160, 160, 160);
    }};
}
