package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;

/**
 * Entities inhabit the GameWorld.  They include items
 * and creatures and can occupy PATH WorldVoxels.
 */
public class Entity {

    private TileDefinition tileDefinition;
    private Position3D position;
    protected String description;

    /**
     * Can you occupy the same space as this entity
     */
    private boolean passable;

    public Entity(TileDefinition tileDefinition, Position3D position) {
        this.position = position;
        this.tileDefinition = tileDefinition;
        this.passable = true;
    }

    public Entity(char sprite, Position3D position) {
        this(TileType.valueOf(sprite).getMetadata(), position);
    }

    /**
     * If this entity has any important GameState logic,
     * run the updation via this method.
     */
    public void update() {
    }

    public TileDefinition getTileDefinition() {
        return tileDefinition;
    }

    public void setTileDefinition(TileDefinition tileDefinition) {
        this.tileDefinition = tileDefinition;
    }

    public Position3D getPosition() {
        return position;
    }

    public void setPosition(Position3D position) {
        this.position = position;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        if (StringUtils.isNotBlank(description)) {
            return description;
        }
        return tileDefinition.getTileType().getDescription();
    }

    public EntityType getType() {
        return EntityType.GENERIC;
    }
}
