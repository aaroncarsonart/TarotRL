package com.aaroncarsonart.tarotrl.util;

import com.aaroncarsonart.tarotrl.exception.TarotRLException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonUtils {

    public static String writeValueAsString(Object obj) {
        try {
            return Globals.OBJECT_WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new TarotRLException(e);
        }
    }

}
