package cc.rapidev.qqbot.database.entity;

import cc.rapidev.qqbot.database.entity.annotations.DBTable;
import cc.rapidev.qqbot.database.entity.annotations.TBColumn;
import cc.rapidev.qqbot.database.entity.annotations.TBPrimaryKey;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author leibrother
 */
public class EntityAnalyzer {

    private static final Map<Class<?>, Table> tables = new HashMap<>();
    private static final Map<Class<?>, String> mapping = new HashMap<>();

    static {
        mapping.put(String.class, "TEXT");
        mapping.put(int.class, "INTEGER");
        mapping.put(Integer.class, "INTEGER");
        mapping.put(short.class, "INTEGER");
        mapping.put(Short.class, "INTEGER");
        mapping.put(long.class, "INTEGER");
        mapping.put(Long.class, "INTEGER");
        mapping.put(Double.class, "REAL");
        mapping.put(Float.class, "REAL");
        mapping.put(boolean.class, "INTEGER");
        mapping.put(Boolean.class, "INTEGER");
        mapping.put(Date.class, "INTEGER");
        mapping.put(LocalDate.class, "INTEGER");
        mapping.put(LocalTime.class, "INTEGER");
        mapping.put(LocalDateTime.class, "INTEGER");
    }

    /**
     * 分析实体
     *
     * @param clazz 实体类
     * @return 表定义
     */
    public static synchronized Table analyze(Class<?> clazz) {
        if (tables.containsKey(clazz)) {
            return tables.get(clazz);
        }
        DBTable dbtable = clazz.getDeclaredAnnotation(DBTable.class);
        if (dbtable == null) {
            throw new RuntimeException("%s is not annotated with %s".formatted(clazz.getName(), DBTable.class.getName()));
        }
        String name = dbtable.name().trim();
        if (name.isEmpty()) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
        }
        Field[] fields = clazz.getDeclaredFields();
        List<TableColumn> columns = new ArrayList<>();
        for (Field field : fields) {
            TableColumn column = analyzeField(field);
            if (column != null) {
                columns.add(column);
            }
        }
        Table table = new Table(name, columns);
        tables.put(clazz, table);
        return table;
    }

    /**
     * 分析实体字段
     *
     * @param field 字段
     * @return 列定义
     */
    private static TableColumn analyzeField(Field field) {
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
        return new TableColumn(
                name,
                type,
                column.notnull(),
                column.defaultValue().trim(),
                primaryKey
        );
    }

}
