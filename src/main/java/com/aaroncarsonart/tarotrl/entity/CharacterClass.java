package com.aaroncarsonart.tarotrl.entity;

public class CharacterClass {

    private ClassType classType;
    private int level;
    private int experience;

    public CharacterClass(ClassType classType) {
        this.classType = classType;
    }

    public String getName() {
        return classType.getClassName();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
