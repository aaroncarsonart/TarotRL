package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.map.PortalTrigger;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.deserializer.TileColorDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Tile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TileDefinition {

    private static final char NULL_CHAR = '\u0000';

    private TileType tileType;

    /**
     * The character used to represent this tile in the {@link GameMapDefinition#mapTerrainData}
     * during the procedural generation/level design phase.
     */
    private char sprite;

    /**
     * The character used to render this tile on the {@link org.hexworks.zircon.api.grid.TileGrid TileGrid}.
     * This has the same value as {@link #sprite} unless overridden.
     */
    private char displaySprite = NULL_CHAR;

    /**
     * Define the foreground color to render this sprite with.
     * <p>
     * See {@link TileColorDeserializer} javadoc for JSON modeling details.
     */
    @JsonDeserialize(using = TileColorDeserializer.class)
    private TileColor foregroundColor;

    /**
     * Define the background color to render this sprite with.
     * <p>
     * See {@link TileColorDeserializer} javadoc for JSON modeling details.
     */
    @JsonDeserialize(using = TileColorDeserializer.class)
    private TileColor backgroundColor;

    private boolean passable;
    private boolean openable;

    private PortalTrigger portalTrigger;

    public TileDefinition() {
    }

    public static TileDefinition custom(char sprite, TileType tileType, TileColor foregroundColor, TileColor backgroundColor) {
        TileDefinition definition = new TileDefinition();
        definition.sprite = sprite;
        definition.tileType = tileType;
        definition.foregroundColor = foregroundColor;
        definition.backgroundColor = backgroundColor;
        return definition;
    }

    public static TileDefinition custom(char sprite, TileType tileType, String foregroundColor, String backgroundColor) {
        TileDefinition definition = new TileDefinition();
        definition.sprite = sprite;
        definition.tileType = tileType;
        definition.foregroundColor = TileColors.fromString(foregroundColor);
        definition.backgroundColor = TileColors.fromString(backgroundColor);
        return definition;
    }

    @JsonIgnore
    private Tile zirconTile;

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public char getSprite() {
        return sprite;
    }

    public void setSprite(char sprite) {
        this.sprite = sprite;
    }

    public char getDisplaySprite() {
        if (displaySprite == NULL_CHAR) {
            return sprite;
        }
        return displaySprite;
    }

    public void setDisplaySprite(char displaySprite) {
        this.displaySprite = displaySprite;
    }

    public TileColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(TileColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public TileColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(TileColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openable) {
        this.openable = openable;
    }

    public PortalTrigger getPortalTrigger() {
        return portalTrigger;
    }

    public void setPortalTrigger(PortalTrigger portalTrigger) {
        this.portalTrigger = portalTrigger;
    }
}
