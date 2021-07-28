package com.aaroncarsonart.tarotrl.util;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Random;

public class Globals {
    private static final Logger LOG = new Logger(Globals.class).withLogLevel(LogLevel.INFO);
    public static final Random RANDOM;
    public static final long RANDOM_SEED_USE_RANDOM = 1;
//    public static long RANDOM_SEED = RANDOM_SEED_USE_RANDOM;
    public static long RANDOM_SEED = 409195412836672360L;
    public static final ObjectMapper OBJECT_MAPPER;
    public static final ObjectWriter OBJECT_WRITER;
    static {
        if (RANDOM_SEED == RANDOM_SEED_USE_RANDOM) {
            Random seeder = new Random();
            RANDOM_SEED = seeder.nextLong();
        }
        LOG.info("RANDOM seed: " + RANDOM_SEED);
        RANDOM = new Random();
        RANDOM.setSeed(RANDOM_SEED);
    }

    static {
        OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        OBJECT_WRITER = OBJECT_MAPPER.writer(prettyPrinter);
    }
}
