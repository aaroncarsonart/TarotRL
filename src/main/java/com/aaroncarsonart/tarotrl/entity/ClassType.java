package com.aaroncarsonart.tarotrl.entity;

public enum ClassType {

    FIGHTER("Fighter"),
    CLERIC("Healer"),
    WIZARD("Wizard"),
    THIEF("Thief");

    private String className;

    ClassType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
