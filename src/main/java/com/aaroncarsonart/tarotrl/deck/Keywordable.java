package com.aaroncarsonart.tarotrl.deck;

import com.aaroncarsonart.tarotrl.util.Globals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Keywordable {

    private List<String> keywords = new ArrayList<>();

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getJsonString() {
        try {
            return Globals.OBJECT_MAPPER.writeValueAsString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
