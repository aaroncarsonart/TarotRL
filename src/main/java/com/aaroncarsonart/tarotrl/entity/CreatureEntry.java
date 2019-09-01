package com.aaroncarsonart.tarotrl.entity;

/**
 * An entry for storing and retrieving CreatureEntity prototypes and instances into
 * spreadsheets or databases.
 */
public class CreatureEntry {
    public enum EntryType {MONSTER, CHARACTER }

    public int id;
    public String name;

    public char sprite;
    public int fgColor;
    public int bgColor;

    public EntryType entryType;

    public int hp;
    public int mp;

    public int attack;
    public int defense;
    public int accuracy;
    public int evade;

    public String actions;
    public String spells;
    public String skills;

    // MONSTER specific stats
    public int rewardedExp;
    public int rewardedGold;
    public String rewardedItems;

    // CHARACTER specific stats
    public String className;
    public int level;
    public int exp;
    public String equipment;
    public String inventory;
}
