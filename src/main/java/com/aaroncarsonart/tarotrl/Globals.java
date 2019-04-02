package com.aaroncarsonart.tarotrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Random;

public class Globals {
    public static final Random RANDOM = new Random();

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
