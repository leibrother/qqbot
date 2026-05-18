package cc.rapidev.qqbot.database.table;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leibrother
 */
public record ColumnDefinition(
        String name,
        String type,
        boolean nullable,
        String defaultValue,
        boolean unique,
        boolean primaryKey
) {

    private static final Map<Class<?>, String> mapping = new HashMap<>();

    static {
        mapping.put(String.class, "TEXT");
        mapping.put(Integer.class, "INTEGER");
        mapping.put(Short.class, "INTEGER");
        mapping.put(Long.class, "INTEGER");
        mapping.put(Double.class, "REAL");
        mapping.put(Float.class, "REAL");
        mapping.put(Boolean.class, "INTEGER");
        mapping.put(Date.class, "TEXT");
        mapping.put(LocalDate.class, "TEXT");
        mapping.put(LocalTime.class, "TEXT");
        mapping.put(LocalDateTime.class, "TEXT");
    }

    public static ColumnDefinition of(Field field) {
        TBColumn column = field.getDeclaredAnnotation(TBColumn.class);
        if (column == null) {
            return null;
        }
        String name = column.name().trim();
        if (name.isEmpty()) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
        String type = column.type().trim();
        if (type.isEmpty()) {
            type = mapping.get(field.getType());
            if (type == null) {
                throw new RuntimeException("type '%s' is not supported for auto-mapping, please specify the SQLite type manually".formatted(field.getType().getName()));
            }
        }
        boolean primaryKey = field.isAnnotationPresent(TBPrimaryKey.class);
        return new ColumnDefinition(
                name,
                type,
                column.nullable(),
                column.defaultValue().trim(),
                !primaryKey && column.unique(),
                primaryKey
        );
    }

    public String schema() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(" ").append(type);
        if (!nullable) {
            builder.append(" NOT NULL");
        }
        if (defaultValue != null && !defaultValue.isEmpty()) {
            builder.append(" DEFAULT ").append(defaultValue);
        }
        if (primaryKey) {
            builder.append(" PRIMARY KEY");
        } else if (unique) {
            builder.append(" UNIQUE");
        }
        return builder.toString();
    }

}
