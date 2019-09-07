package com.aaroncarsonart.tarotrl.map;

import com.aaroncarsonart.tarotrl.graphics.GameColors;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.map.json.TileDefinitionSet;
import org.hexworks.zircon.api.color.TileColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileDefinitionSets {

    private static TileDefinitionSet getTileDefinitionSet(TileColor color,
                                                          TileColor color1,
                                                          TileColor color2,
                                                          TileColor color3,
                                                          TileColor color4) {
        TileDefinitionSet set = new TileDefinitionSet();
        List<TileDefinition> definitions = new ArrayList<>();

        definitions.add(new TileDefinition('@', TileType.PLAYER, color, color2, false, false, null));
        definitions.add(new TileDefinition('.', TileType.PATH, color1, color2, true, false, null));
        definitions.add(new TileDefinition('#', TileType.WALL, color3, color4, false, false, null));
        definitions.add(new TileDefinition('-', TileType.OPEN_DOOR, GameColors.YELLOW, color2, true, false, null));
        definitions.add(new TileDefinition('+', TileType.CLOSED_DOOR, GameColors.YELLOW, color4, false, true, null));
        definitions.add(new TileDefinition('=', TileType.WINDOW, GameColors.CYAN, color4, false, false, null));
        definitions.add(new TileDefinition('<', TileType.UPSTAIRS, color, color2, true, false, PortalTrigger.ON_INSPECT_TILE));
        definitions.add(new TileDefinition('>', TileType.DOWNSTAIRS, color, color2, true, false, PortalTrigger.ON_INSPECT_TILE));
        definitions.add(new TileDefinition('O', TileType.FURNITURE, color1, color2, false, false, null));
        definitions.add(new TileDefinition('0', TileType.PORTAL, GameColors.BLACK, GameColors.BLACK, true, false, PortalTrigger.ON_OCCUPY_TILE));
        definitions.add(new TileDefinition('%', TileType.PORTAL, GameColors.CYAN, color2, true, false, PortalTrigger.ON_INSPECT_TILE));
        definitions.add(new TileDefinition(' ', TileType.EMPTY, GameColors.BLACK, GameColors.BLACK, false, false, null));
        definitions.add(new TileDefinition('C', TileType.ENTITY, GameColors.RED, color2, false, false, null));
        definitions.add(new TileDefinition('^', TileType.ITEM, GameColors.GREY, color2, false, false, null));
        definitions.add(new TileDefinition('$', TileType.TREASURE, GameColors.YELLOW, color2, false, false, null));

        Map<Character, TileDefinition> definitionsMap = new HashMap<>();
        for (TileDefinition definition : definitions) {
            definitionsMap.put(definition.getSprite(), definition);
        }
        set.setTileDefinitions(definitionsMap);
        return set;

    }

    public static TileDefinitionSet getBlueTileDefinitionSet() {
        TileColor color = GameColors.CYAN;    // FF
        TileColor color1 = GameColors.CYAN_1; // C8
        TileColor color2 = GameColors.CYAN_2; // 32
        TileColor color3 = GameColors.CYAN_3; // 2A
        TileColor color4 = GameColors.CYAN_4; // 11
        return getTileDefinitionSet(color, color1, color2, color3, color4);
    }

    public static TileDefinitionSet getRedTileDefinitionSet() {
        TileColor color = GameColors.RED;    // FF
        TileColor color1 = GameColors.RED_1; // C8
        TileColor color2 = GameColors.RED_2; // 32
        TileColor color3 = GameColors.RED_3; // 2A
        TileColor color4 = GameColors.RED_4; // 11
        return getTileDefinitionSet(color, color1, color2, color3, color4);
    }

    public static TileDefinitionSet getYellowTileDefinitionSet() {
        TileColor color = GameColors.YELLOW;    // FF
        TileColor color1 = GameColors.YELLOW_1; // C8
        TileColor color2 = GameColors.YELLOW_2; // 32
        TileColor color3 = GameColors.YELLOW_3; // 2A
        TileColor color4 = GameColors.YELLOW_4; // 11
        return getTileDefinitionSet(color, color1, color2, color3, color4);
    }

    public static TileDefinitionSet getGreenTileDefinitionSet() {
        TileColor color = GameColors.GREEN;    // FF
        TileColor color1 = GameColors.GREEN_1; // C8
        TileColor color2 = GameColors.GREEN_2; // 32
        TileColor color3 = GameColors.GREEN_3; // 2A
        TileColor color4 = GameColors.GREEN_4; // 11
        return getTileDefinitionSet(color, color1, color2, color3, color4);
    }
}
