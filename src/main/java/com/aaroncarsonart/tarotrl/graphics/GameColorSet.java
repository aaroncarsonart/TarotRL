package com.aaroncarsonart.tarotrl.graphics;

import org.hexworks.zircon.api.color.TileColor;

public class GameColorSet {
    public static TileColor COLOR;
    public static TileColor COLOR_1;
    public static TileColor COLOR_2;
    public static TileColor COLOR_3;
    public static TileColor COLOR_4;

    public static void setGameColorSetColors(GameColorSet set) {
        COLOR = set.color;
        COLOR_1 = set.color2;
        COLOR_2 = set.color2;
        COLOR_3 = set.color3;
        COLOR_4 = set.color4;
    }

    public static final GameColorSet GREEN = new GameColorSet(
            GameColors.GREEN,
            GameColors.GREEN_1,
            GameColors.GREEN_2,
            GameColors.GREEN_3,
            GameColors.GREEN_4,
            "tile_definitions/forest_green.json");

    public static final GameColorSet YELLOW = new GameColorSet(
            GameColors.YELLOW,
            GameColors.YELLOW_1,
            GameColors.YELLOW_2,
            GameColors.YELLOW_3,
            GameColors.YELLOW_4,
            "tile_definitions/yellow_gold.json");

    public static final GameColorSet CYAN = new GameColorSet(
            GameColors.CYAN,
            GameColors.CYAN_1,
            GameColors.CYAN_2,
            GameColors.CYAN_3,
            GameColors.CYAN_4,
            "tile_definitions/sea_cyan.json");

    public static final GameColorSet RED = new GameColorSet(
            GameColors.RED,
            GameColors.RED_1,
            GameColors.RED_2,
            GameColors.RED_3,
            GameColors.RED_4,
            "tile_definitions/mountain_red.json");

    public static final GameColorSet MAGENTA = new GameColorSet(
            GameColors.MAGENTA,
            GameColors.MAGENTA_1,
            GameColors.MAGENTA_2,
            GameColors.MAGENTA_3,
            GameColors.MAGENTA_4,
            "tile_definitions/arcana_magenta.json");

    public static final GameColorSet GREY = new GameColorSet(
            GameColors.GREY,
            GameColors.GREY_1,
            GameColors.GREY_2,
            GameColors.GREY_3,
            GameColors.GREY_4,
            "tile_definitions/default_grey.json");

    public final TileColor color;
    public final TileColor color1;
    public final TileColor color2;
    public final TileColor color3;
    public final TileColor color4;
    public final String tileDefinition;

    public GameColorSet(TileColor color,
                        TileColor color1,
                        TileColor color2,
                        TileColor color3,
                        TileColor color4,
                        String tileDefinition) {
        this.color = color;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.tileDefinition = tileDefinition;
    }
}
