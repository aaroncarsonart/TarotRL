package com.aaroncarsonart.tarotrl.graphics;

import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;

/**
 * Encapsulate the specific colors used to render GameMaps.
 */
public class GameColors {

    public static final TileColor TRANSPARENT = TileColors.create(0, 0, 0, 0);

    public static final TileColor BLACK = TileColors.create(0, 0, 0);
    public static final TileColor WHITE = TileColors.create(255, 255, 255);

    public static final TileColor LIGHT_GRAY = TileColors.create(192, 192, 192); // C0C0C0
    public static final TileColor GRAY       = TileColors.create(128, 128, 128); // 808080
    public static final TileColor DARK_GRAY  = TileColors.create(64, 64, 64);    // 404040
    public static final TileColor DARKER_GRAY = TileColors.create(32, 32, 32);

    public static final TileColor RED   = TileColors.create(255, 0, 0);
    public static final TileColor GREEN = TileColors.create(0, 255, 0);
    public static final TileColor BLUE  = TileColors.create(0, 0, 255);

    public static final TileColor YELLOW  = TileColors.create(255, 255, 0);
    public static final TileColor CYAN    = TileColors.create(0, 255, 255);
    public static final TileColor MAGENTA = TileColors.create(255, 0, 255);

    public static final TileColor RED_1   = TileColors.fromString("#C80000");
    public static final TileColor RED_2   = TileColors.fromString("#320000");
    public static final TileColor RED_3   = TileColors.fromString("#2A0000");
    public static final TileColor RED_4   = TileColors.fromString("#110000");

    public static final TileColor GREEN_1   = TileColors.fromString("#00C800");
    public static final TileColor GREEN_2   = TileColors.fromString("#003200");
    public static final TileColor GREEN_3   = TileColors.fromString("#002A00");
    public static final TileColor GREEN_4   = TileColors.fromString("#001100");

    public static final TileColor BLUE_1   = TileColors.fromString("#6464C8");
    public static final TileColor BLUE_2   = TileColors.fromString("#000032");
    public static final TileColor BLUE_3   = TileColors.fromString("#00002A");
    public static final TileColor BLUE_4   = TileColors.fromString("#000011");
//    public static final TileColor BLUE_2   = TileColors.fromString("#484832");
//    public static final TileColor BLUE_3   = TileColors.fromString("#32322A");
//    public static final TileColor BLUE_4   = TileColors.fromString("#161611");

//    public static final TileColor BLUE_1   = TileColors.fromString("#0000F8");
//    public static final TileColor BLUE_2   = TileColors.fromString("#000062");
//    public static final TileColor BLUE_3   = TileColors.fromString("#00004A");
//    public static final TileColor BLUE_4   = TileColors.fromString("#000021");

    public static final TileColor YELLOW_1   = TileColors.fromString("#C8C800");
    public static final TileColor YELLOW_2   = TileColors.fromString("#323200");
    public static final TileColor YELLOW_3   = TileColors.fromString("#2A2A00");
    public static final TileColor YELLOW_4   = TileColors.fromString("#111100");

    public static final TileColor CYAN_1   = TileColors.fromString("#00C8C8");
    public static final TileColor CYAN_2   = TileColors.fromString("#003232");
    public static final TileColor CYAN_3   = TileColors.fromString("#002A2A");
    public static final TileColor CYAN_4   = TileColors.fromString("#001111");

    public static final TileColor CYAN_5   = TileColors.fromString("#008787");

    public static final TileColor MAGENTA_1   = TileColors.fromString("#C800C8");
    public static final TileColor MAGENTA_2   = TileColors.fromString("#320032");
    public static final TileColor MAGENTA_3   = TileColors.fromString("#2A002A");
    public static final TileColor MAGENTA_4   = TileColors.fromString("#110011");

    public static final TileColor GREY     = TileColors.fromString("#FFFFFF");
    public static final TileColor GREY_1   = TileColors.fromString("#C8C8C8");
    public static final TileColor GREY_2   = TileColors.fromString("#323232");
    public static final TileColor GREY_3   = TileColors.fromString("#2A2A2A");
    public static final TileColor GREY_4   = TileColors.fromString("#111111");

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
