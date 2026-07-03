package cc.rapidev.qqbot.database.repository.parameter;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter
@NoArgsConstructor
@DBTable(name = "bot_parameters")
public class ParameterEntity {

    @TBColumn
    @TBPrimaryKey
    private String key;

    @TBColumn(notnull = true)
    private String type;

    @TBColumn(notnull = true)
    private String value;

    private ParameterEntity(String key, String type, String value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public ParameterEntity(String key, ParameterType type, Object value) {
        this(key, type.name(), type.getEncoder().apply(value));
    }

    public Object resolve() {
        return ParameterType.valueOf(type).getDecoder().apply(value);
    }

    public static ParameterEntity of(String key, Object value) {
        return switch (value) {
            case Number ignore -> new ParameterEntity(key, ParameterType.NUMBER, value);
            case String ignore -> new ParameterEntity(key, ParameterType.STRING, value);
            case Boolean ignore -> new ParameterEntity(key, ParameterType.BOOLEAN, value);
            default -> new ParameterEntity(key, ParameterType.JSON, value);
        };
    }

}
