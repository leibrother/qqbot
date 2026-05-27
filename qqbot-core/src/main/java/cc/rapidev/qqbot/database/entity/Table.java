package cc.rapidev.qqbot.database.entity;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Table {

    private final String name;
    private final List<TableColumn> columns;
    private final List<TableColumn> primaryKeys;

    public Table(String name, List<TableColumn> columns) {
        this.name = name;
        this.columns = columns;
        this.primaryKeys = columns.stream().filter(TableColumn::primaryKey).toList();
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
     * 安全的表名称
     *
     * @return 'name'
     */
    public String safename() {
        return StringUtils.packing("'", name);
    }

    /**
     * 生成建表语句
     *
     * @return SQL
     */
    public String schema() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(StringUtils.packing("'", name)).append(" (");
        builder.append(this.columns.stream().map(TableColumn::schema).collect(Collectors.joining(", ")));
        if (!this.primaryKeys.isEmpty()) {
            builder.append(", ");
            builder.append("PRIMARY KEY (");
            builder.append(this.primaryKeys.stream().map(TableColumn::safename).collect(Collectors.joining(", ")));
            builder.append(")");
        }
        builder.append(");");
        return builder.toString();
    }

    /**
     * 生成数据库表差异更新语句
     *
     * @param columns 原表字段
     * @return SQL列表
     */
    public List<String> diffUpdate(List<TableColumn> columns) {
        Map<String, TableColumn> curr = this.columns.stream().collect(Collectors.toMap(TableColumn::name, Function.identity()));
        Map<String, TableColumn> prev = columns.stream().collect(Collectors.toMap(TableColumn::name, Function.identity()));
        // 被删除的列
        List<String> removes = prev.keySet()
                .stream()
                .filter(name -> !curr.containsKey(name))
                .toList();
        // 有变更的列
        List<String> changes = prev.values()
                .stream()
                .filter(column -> !removes.contains(column.name()))
                .filter(column -> !column.equals(curr.get(column.name())))
                .map(TableColumn::name)
                .toList();
        // 新增的主键列
        List<String> newPrimaryKeys = curr.keySet()
                .stream()
                .filter(name -> !prev.containsKey(name))
                .filter(name -> curr.get(name).primaryKey())
                .toList();
        // 如果有字段删除或者修改或者主键列变更，则需要重建表
        if (!removes.isEmpty() || !changes.isEmpty() || !newPrimaryKeys.isEmpty()) {
            List<String> sqls = new ArrayList<>();
            // 将旧表重命名
            String rename = "'%s_bak_%s'".formatted(name(), IdentityUtils.shortID());
            sqls.add("ALTER TABLE %s RENAME TO %s;".formatted(safename(), rename));
            // 创建新表
            sqls.add(schema());
            // 转移数据
            List<String> eqs = prev.keySet().stream().filter(name -> !removes.contains(name) && !changes.contains(name)).toList();
            List<String> casts = changes.stream().map(name -> "CAST(%s AS %s)".formatted(name, curr.get(name).type())).toList();
            if (!eqs.isEmpty() || !casts.isEmpty()) {
                String cols = Stream.concat(eqs.stream(), changes.stream()).collect(Collectors.joining(", "));
                String select = Stream.concat(eqs.stream(), casts.stream()).collect(Collectors.joining(", "));
                sqls.add("INSERT INTO %s (%s) SELECT %s FROM %s;".formatted(safename(), cols, select, rename));
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
                    .map(col -> "ALTER TABLE %s ADD COLUMN %s;".formatted(safename(), col.schema()))
                    .toList();
        }
        return List.of();
    }

}
