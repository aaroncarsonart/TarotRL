package com.aaroncarsonart.tarotrl.map.json.deserializer;

import com.aaroncarsonart.tarotrl.util.Globals;
import com.aaroncarsonart.tarotrl.map.json.TileDefinition;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class TileDefinitionMapDeserializer extends JsonDeserializer<HashMap<Character, TileDefinition>> {

    private ObjectMapper mapper = Globals.OBJECT_MAPPER;

    @Override
    public HashMap<Character, TileDefinition> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

        ObjectCodec codec = parser.getCodec();
        JsonNode root = codec.readTree(parser);

        if(root.isArray()) {
            HashMap<Character, TileDefinition> map = new HashMap<>();
//            ArrayNode jsonArray = (ArrayNode) root;
            for (JsonNode element : root) {
                TileDefinition tileDefinition = mapper.treeToValue(element, TileDefinition.class);
                Character key = tileDefinition.getSprite();
                map.put(key, tileDefinition);
            }
            return map;
        }
        return null;
    }
}
