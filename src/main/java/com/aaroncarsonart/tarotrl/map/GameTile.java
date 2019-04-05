package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.tarotrl.game.zircon.GameColors;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;

import java.util.HashMap;
import java.util.Map;

public class GameTile {

    private static final GameColors colors = GameColors.FOREST_GREEN;
    private Map<Character, GameTile> tileMap = new HashMap<>();

    public static final GameTile PLAYER = gameTile('@', colors.player, colors.bgOpen);
    public static final GameTile WALL   = gameTile('#', colors.fgWall, colors.bgWall);
    public static final GameTile PATH   = gameTile('.', colors.fgOpen, colors.bgOpen);
    public static final GameTile EMPTY  = gameTile(' ', colors.fgEmpty, colors.bgEmpty);

    public static final GameTile DOWNSTAIRS  = gameTile('>', GameColors.GREEN, colors.bgOpen);
    public static final GameTile UPSTAIRS    = gameTile('<', GameColors.GREEN, colors.bgOpen);
    public static final GameTile CLOSED_DOOR = gameTile('+', GameColors.YELLOW, colors.bgOpen);
    public static final GameTile OPEN_DOOR   = gameTile('-', GameColors.YELLOW, colors.bgOpen);
    public static final GameTile WINDOW      = gameTile('=', GameColors.YELLOW, colors.bgOpen);
    public static final GameTile TREASURE    = gameTile('$', GameColors.YELLOW, colors.bgOpen);

    public static final GameTile UNKNOWN = gameTile('?', GameColors.WHITE, GameColors.MAGENTA);

    private char character;
    private TileColor fg;
    private TileColor bg;

    private Tile tile;

    GameTile(char character, TileColor fg, TileColor bg) {
        this.character = character;
        this.fg = fg;
        this.bg = bg;
        this.tile = Tiles.newBuilder()
                .withBackgroundColor(bg)
                .withForegroundColor(fg)
                .withCharacter(character)
                .build();

    }

    private static GameTile gameTile(char character, TileColor fg, TileColor bg) {
        return new GameTile(character, fg, bg);
    }



    public char getCharacter() {
        return character;
    }

    public TileColor getFgColor() {
        return fg;
    }

    public TileColor getBgColor() {
        return bg;
    }

    public Tile getTile() {
        return tile;
    }
}
