package com.aaroncarsonart.tarotrl.map.json;

import com.aaroncarsonart.tarotrl.Globals;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.hexworks.zircon.api.TileColors;
import org.hexworks.zircon.api.color.TileColor;

import java.io.IOException;

/**
 * Supports two forms of custom definitions:
 * <ol>
 *     <li>
 *         A hexadecimal String, i.e:
 *         <pre>"#0044ff"</pre>
 *     </li>
 *     <li>
 *         An object with RGBA values: (see {@link RgbaColorDefinition})
 *         <pre>{ "r": 255, "g": 255, "b": 255 }</pre>
 *     </li>
 * </ol>
 */
public class TileColorDeserializer extends JsonDeserializer<TileColor> {

    private ObjectMapper mapper = Globals.OBJECT_MAPPER;

    @Override
    public TileColor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.getNodeType() == JsonNodeType.OBJECT) {
            RgbaColorDefinition definition = mapper.treeToValue(node, RgbaColorDefinition.class);
            return definition.toTileColor();
        }
        if (node.getNodeType() == JsonNodeType.STRING) {
            String value = mapper.treeToValue(node, String.class);
            return TileColors.fromString(value);
        }

        return null;
    }
}
