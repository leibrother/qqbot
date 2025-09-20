package cc.rapidev.qqbot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author leibrother
 */
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        // qqbot的接口都使用的下划线命名，因此本项目也使用下划线命名方案
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectNode node() {
        return mapper.createObjectNode();
    }

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convert(JsonNode node, Class<T> clazz) {
        return mapper.convertValue(node, clazz);
    }

    public static <T> T convert(JsonNode node, TypeReference<T> type) {
        return mapper.convertValue(node, type);
    }

}
