package com.aaroncarsonart.tarotrl.entity;

import com.aaroncarsonart.tarotrl.map.TileType;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.aaroncarsonart.tarotrl.world.Position3D;

import java.util.Map;

public class CreatureEntity extends MapEntity {
    private Map<Stat, Object> stats;

    public CreatureEntity(TileDefinition tileDefinition, Position3D position) {
        super(tileDefinition, position);
    }
    public CreatureEntity() {
        super(TileDefinition.custom('C', TileType.EMPTY, "112233", "112233"), Position3D.ORIGIN);
    }

    public int getIntStat(Stat stat) {
        Integer intStat = (Integer) stats.get(stat);
        return intStat.intValue();
    }

    public double getDoubleStat(Stat stat) {
        Double doubleStat = (Double) stats.get(stat);
        return doubleStat.doubleValue();
    }

    public String getStringStat(Stat stat) {
        String stringStat = (String) stats.get(stat);
        return stringStat;
    }

    public Object getStat(Stat stat) {
        return stats.get(stat);
    }

    public void setStat(Stat stat, Object value) {
        stats.put(stat, value);
    }
}
