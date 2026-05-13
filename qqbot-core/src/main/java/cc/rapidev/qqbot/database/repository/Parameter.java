package cc.rapidev.qqbot.database.repository;

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
public class Parameter {

    @TBColumn
    @TBPrimaryKey
    private String key;

    @TBColumn(nullable = false)
    private String type;

    @TBColumn(nullable = false)
    private String value;

    private Parameter(String key, String type, String value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    public Parameter(String key, ParameterType type, Object value) {
        this(key, type.name(), type.getEncoder().apply(value));
    }

    public static Parameter of(String key, Object value) {
        return switch (value) {
            case Number ignore -> new Parameter(key, ParameterType.NUMBER, value);
            case String ignore -> new Parameter(key, ParameterType.STRING, value);
            case Boolean ignore -> new Parameter(key, ParameterType.BOOLEAN, value);
            default -> new Parameter(key, ParameterType.JSON, value);
        };
    }

}
