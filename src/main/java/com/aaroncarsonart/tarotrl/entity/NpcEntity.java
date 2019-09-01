package com.aaroncarsonart.tarotrl.entity;

/**
 * Base class for all PC's (player characters) and NPC's (non-player characters).
 * Alternate inheritance path for {@link MonsterEntity} that don't have attributes
 * such as experience, level, class etc.
 */
public class NpcEntity extends CreatureEntity {
    private CharacterClass characterClass;
}
