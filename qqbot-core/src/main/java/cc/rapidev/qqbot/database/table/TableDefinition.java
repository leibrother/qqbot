package cc.rapidev.qqbot.database.table;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class TableDefinition {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String name;
    private final List<ColumnDefinition> columns;
    private final String primaryKey;

    public TableDefinition(String name, List<ColumnDefinition> columns) {
        this.name = name;
        this.columns = columns;
        List<ColumnDefinition> primaryKeys = columns.stream().filter(ColumnDefinition::primaryKey).toList();
        if (primaryKeys.isEmpty()) {
            this.primaryKey = null;
            logger.warn("table '{}' has no primary key set, some features may not be available", name);
        } else if (primaryKeys.size() > 1) {
            throw new RuntimeException("Only one primary key can be defined");
        } else {
            this.primaryKey = primaryKeys.getFirst().name();
        }
    }

    public static TableDefinition of(Class<?> clazz) {
        DBTable table = clazz.getDeclaredAnnotation(DBTable.class);
        if (table == null) {
            throw new RuntimeException("%s is not annotated with %s".formatted(clazz.getName(), DBTable.class.getName()));
        }
        String name = table.name().trim();
        if (name.isEmpty()) {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
        }
        Field[] fields = clazz.getDeclaredFields();
        List<ColumnDefinition> columns = new ArrayList<>();
        for (Field field : fields) {
            ColumnDefinition column = ColumnDefinition.of(field);
            if (column != null) {
                columns.add(column);
            }
        }
        return new TableDefinition(name, columns);
    }

    /**
     * 表名称
     *
     * @return name
     */
    public String name() {
        return this.name;
    }

    /**
     * 生成建表语句
     *
     * @return SQL
     */
    public String schema() {
        String columns = this.columns.stream().map(ColumnDefinition::schema).collect(Collectors.joining(", "));
        return "CREATE TABLE %s(%s);".formatted(name, columns);
    }

    /**
     * 生成数据库表差异更新语句
     *
     * @param columns 原表字段
     * @return SQL列表
     */
    public List<String> diffUpdate(List<ColumnDefinition> columns) {
        Map<String, ColumnDefinition> curr = this.columns.stream().collect(Collectors.toMap(ColumnDefinition::name, Function.identity()));
        Map<String, ColumnDefinition> prev = columns.stream().collect(Collectors.toMap(ColumnDefinition::name, Function.identity()));
        // 检查是否需要重建
        List<String> removes = prev.keySet().stream().filter(name -> !curr.containsKey(name)).toList();
        List<String> changes = this.columns.stream().filter(column -> !column.schema().equals(column.schema())).map(ColumnDefinition::name).toList();
        if (!removes.isEmpty() || !changes.isEmpty()) {
            // 有字段删除，需要重建
            List<String> sqls = new ArrayList<>();
            // 将旧表重命名
            String rename = "%s_bak_%s".formatted(name, IdentityUtils.shortID());
            sqls.add("ALTER TABLE %s RENAME TO %s;".formatted(name, rename));
            // 创建新表
            sqls.add(schema());
            // 转移数据
            List<String> eqs = prev.keySet().stream().filter(name -> !removes.contains(name) && !changes.contains(name)).toList();
            List<String> casts = changes.stream().map(name -> "CAST(%s AS %s)".formatted(name, curr.get(name).type())).toList();
            if (!eqs.isEmpty() || !casts.isEmpty()) {
                String cols = Stream.concat(eqs.stream(), changes.stream()).collect(Collectors.joining(", "));
                String select = Stream.concat(eqs.stream(), casts.stream()).collect(Collectors.joining(", "));
                sqls.add("INSERT INTO %s (%s) SELECT %s FROM %s;".formatted(name, cols, select, rename));
            }
            // 删除旧表
            sqls.add("DROP TABLE %s;".formatted(rename));
            return sqls;
        }
        List<String> news = curr.keySet().stream().filter(name -> !prev.containsKey(name)).toList();
        if (!news.isEmpty()) {
            // 添加字段
            return news.stream()
                    .map(curr::get)
                    .map(col -> "ALTER TABLE %s ADD COLUMN %s;".formatted(name, col.schema()))
                    .toList();
        }
        return List.of();
    }

}
