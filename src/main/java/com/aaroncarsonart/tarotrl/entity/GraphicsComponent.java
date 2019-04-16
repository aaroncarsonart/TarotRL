package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.map.TileType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Tile;

public class GraphicsComponent {
    private static final GraphicsComponent DEFAULT = new GraphicsComponent(
            TileType.EMPTY.getSprite(),
            TileType.EMPTY.getMetadata().getForegroundColor(),
            TileType.EMPTY.getMetadata().getBackgroundColor());

    private char sprite;
    private TileColor foregroundColor;
    private TileColor backgroundColor;

    private transient Tile tile;
    private transient boolean dirty;

    public GraphicsComponent(char sprite, TileColor foregroundColor, TileColor backgroundColor) {
        this.sprite = sprite;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }

    public GraphicsComponent(char sprite) {
        this(sprite, DEFAULT.foregroundColor, DEFAULT.backgroundColor);
    }

    public void render(GraphicsContext context, Position position) {
        if (dirty) {
            tile = buildTile();
        }
//        TileGrid tileGrid = context.getTileGrid();
//        tileGrid.setTileAt(position, tile);
    }

    private Tile buildTile() {
        return Tiles.newBuilder()
                .withCharacter(sprite)
                .withForegroundColor(foregroundColor)
                .withBackgroundColor(backgroundColor)
                .build();
    }

    public char getSprite() {
        return sprite;
    }

    public void setSprite(char sprite) {
        this.sprite = sprite;
        setDirty();
    }

    public TileColor getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(TileColor foregroundColor) {
        this.foregroundColor = foregroundColor;
        setDirty();
    }

    public TileColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(TileColor backgroundColor) {
        this.backgroundColor = backgroundColor;
        setDirty();
    }

    private void setDirty() {
        this.dirty = true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(sprite)
                .append(foregroundColor.getRed())
                .append(foregroundColor.getGreen())
                .append(foregroundColor.getBlue())
                .append(foregroundColor.getAlpha())
                .append(backgroundColor.getRed())
                .append(backgroundColor.getGreen())
                .append(backgroundColor.getBlue())
                .append(backgroundColor.getAlpha())
                .build();
    }

    public boolean equals(Object obj) {
        if (obj instanceof GraphicsComponent) {
            GraphicsComponent other = (GraphicsComponent) obj;
            return new EqualsBuilder()
                    .append(this.sprite, other.sprite)
                    .append(this.foregroundColor.getRed(),   other.foregroundColor.getRed())
                    .append(this.foregroundColor.getGreen(), other.foregroundColor.getGreen())
                    .append(this.foregroundColor.getBlue(),  other.foregroundColor.getBlue())
                    .append(this.foregroundColor.getAlpha(), other.foregroundColor.getAlpha())
                    .append(this.foregroundColor.getRed(),   other.backgroundColor.getRed())
                    .append(this.foregroundColor.getGreen(), other.backgroundColor.getGreen())
                    .append(this.foregroundColor.getBlue(),  other.backgroundColor.getBlue())
                    .append(this.foregroundColor.getAlpha(), other.backgroundColor.getAlpha())
                    .build();
        }
        return false;
    }
}
