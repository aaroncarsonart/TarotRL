package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.game.GameState;
import com.aaroncarsonart.tarotrl.graphics.GraphicsContext;
import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.Position3D;
import org.apache.commons.lang3.StringUtils;

/**
 * MapEntities inhabit the GameMap3D.  They include items
 * and creatures and can occupy PATH WorldVoxels.
 *
 * All entities have a position, description, graphics component, and a status.
 */
public class MapEntity {

    private TileDefinition tileDefinition;
    private Position3D position;
    protected String description;
    private int stepCount;
    private String status;

    /**
     * Can you occupy the same space as this entity
     */
    private boolean passable;

    public MapEntity(TileDefinition tileDefinition, Position3D position) {
        this.position = position;
        this.tileDefinition = tileDefinition;
        this.passable = true;
    }

    public MapEntity(char sprite, Position3D position) {
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

    public boolean isIgnorePassable() {
        return false;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void incrementStepCount() {
        this.stepCount += 1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void update(GameState gameState) {
    }

    public void render(GraphicsContext graphicsContext) {

    }
}
