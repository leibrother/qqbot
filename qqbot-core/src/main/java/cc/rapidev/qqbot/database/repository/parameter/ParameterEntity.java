package cc.rapidev.qqbot.database.repository.parameter;

import cc.rapidev.qqbot.database.table.DBTable;
import cc.rapidev.qqbot.database.table.TBColumn;
import cc.rapidev.qqbot.database.table.TBPrimaryKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author leibrother
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@DBTable(name = "bot_parameters")
public class ParameterEntity {

    @TBColumn
    @TBPrimaryKey
    private String key;

    @TBColumn(nullable = false)
    private String type;

    @TBColumn(nullable = false)
    private String value;

    private ParameterEntity(String key, String type, String value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public ParameterEntity(String key, ParameterType type, Object value) {
        this(key, type.name(), type.getEncoder().apply(value));
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
