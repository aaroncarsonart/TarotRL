package com.aaroncarsonart.tarotrl.util;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Random;

public class Globals {
    public static final Random RANDOM = new Random(1);
    public static final ObjectMapper OBJECT_MAPPER;
    public static final ObjectWriter OBJECT_WRITER;

    static {
        OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        OBJECT_WRITER = OBJECT_MAPPER.writer(prettyPrinter);
    }
}
