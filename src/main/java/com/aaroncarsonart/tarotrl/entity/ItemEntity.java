package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.inventory.GameItem;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;

public class ItemEntity extends MapEntity {

    private GameItem item;

    public ItemEntity(Position3D position, GameItem item) {
        this(TileType.ITEM, position, item);
    }

    public ItemEntity(TileType tileType, Position3D position, GameItem item) {
        super(tileType.getSprite(), position);
        this.item = item;
    }

    public ItemEntity(TileType tileType, Position3D position, GameItem item, String customDescription) {
        super(tileType.getSprite(), position);
        this.item = item;
        this.description = customDescription;
    }

    public ItemEntity(TileDefinition tileDefinition, Position3D position, GameItem item, String customDescription) {
        super(tileDefinition, position);
        this.item = item;
        this.description = customDescription;
    }


    public GameItem getItem() {
        return item;
    }

    public void setItem(GameItem item) {
        this.item = item;
    }

    public String getDescription() {
        if (StringUtils.isNotBlank(description)) {
            return description;
        }
        String itemDescription = item.getDescription();
        if (StringUtils.isNotBlank(itemDescription)) {
            return "there lies " + item.getName();
        }
        return getTileDefinition().getTileType().getDescription();
    }

    public EntityType getType() {
        return EntityType.ITEM;
    }
}
