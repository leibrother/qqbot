package cc.rapidev.qqbot.database.repository;

import cc.rapidev.qqbot.common.utils.JsonUtils;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author leibrother
 */
public enum ParameterType {

    STRING(Object::toString, (value) -> value),
    NUMBER(Object::toString, (value) -> value.contains(".") ? Double.parseDouble(value) : Long.parseLong(value)),
    BOOLEAN(Object::toString, Boolean::parseBoolean),
    JSON(JsonUtils::toJson, value -> JsonUtils.fromJson(value, Object.class)),
    ;

    @Getter
    private final Function<Object, String> encoder;
    @Getter
    private final Function<String, Object> decoder;

    ParameterType(Function<Object, String> encoder, Function<String, Object> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

}
