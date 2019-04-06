package com.aaroncarsonart.tarotrl.util;

public enum NounType {
    SINGULAR, PLURAL;

    public String applyArticleTo(String noun) {
        switch (this) {
            case SINGULAR: return "a " + noun;
            case PLURAL: return noun;
        }
        // Yay, impossible code!
        return null;
    }
}
