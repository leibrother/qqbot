package cc.rapidev.qqbot.database.entity;

import cc.rapidev.qqbot.common.utils.IdentityUtils;
import cc.rapidev.qqbot.common.utils.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leibrother
 */
public class Table {

    private final String name;
    private final List<TableColumn> pks;
    private final List<TableColumn> columns;
    private final Map<SQL, String> sqls = new HashMap<>();

    public Table(String name, List<TableColumn> columns) {
        this.name = name;
        this.pks = columns.stream().filter(TableColumn::primaryKey).toList();
        this.columns = columns;
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
        return StringUtils.packing("`", name);
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
        if (!this.pks.isEmpty()) {
            builder.append(", ");
            builder.append("PRIMARY KEY (");
            builder.append(this.pks.stream().map(TableColumn::safename).collect(Collectors.joining(", ")));
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

    public String getSQL(SQL SQL) {
        if (!this.sqls.containsKey(SQL)) {
            synchronized (this) {
                if (!this.sqls.containsKey(SQL)) {
                    String sql = SQL.getGenerator().apply(this);
                    this.sqls.put(SQL, sql);
                }
            }
        }
        return sqls.get(SQL);
    }

    private String whereByPrimaryKeys() {
        if (this.pks.isEmpty()) {
            throw new IllegalStateException("没有主键字段，无法生成SQL语句");
        }
        StringBuilder where = new StringBuilder();
        where.append(" WHERE ");
        Iterator<TableColumn> pks = this.pks.iterator();
        while (pks.hasNext()) {
            TableColumn pk = pks.next();
            where.append(pk.safename()).append(" = :").append(pk.name());
            if (pks.hasNext()) {
                where.append(" AND ");
            }
        }
        return where.toString();
    }

    public String generateSelectSQL() {
        return "SELECT * FROM " + safename();
    }

    public String generateCountSQL() {
        return "SELECT COUNT(*) FROM " + safename();
    }

    public String generateExistsByPrimaryKeysSQL() {
        return "SELECT COUNT(*) FROM " + safename() + whereByPrimaryKeys();
    }

    public String generateInsertSQL() {
        StringBuilder cols = new StringBuilder();
        StringBuilder bind = new StringBuilder();
        Iterator<TableColumn> columns = this.columns.iterator();
        while (columns.hasNext()) {
            TableColumn column = columns.next();
            cols.append(column.safename());
            bind.append(":").append(column.name());
            if (columns.hasNext()) {
                cols.append(", ");
                bind.append(", ");
            }
        }
        return "INSERT INTO " + safename() + " (" + cols + " ) VALUES (" + bind + ");";
    }

    public String generateUpdateByPrimaryKeysSQL() {
        StringBuilder set = new StringBuilder();
        Iterator<TableColumn> columns = this.columns.stream().filter(col -> !col.primaryKey()).iterator();
        while (columns.hasNext()) {
            TableColumn column = columns.next();
            set.append(column.safename()).append(" = :").append(column.name());
            if (columns.hasNext()) {
                set.append(", ");
            }
        }
        return "UPDATE " + safename() + " SET " + set + whereByPrimaryKeys();
    }

    public String generateDeleteByPrimaryKeysSQL() {
        return "DELETE FROM " + safename() + whereByPrimaryKeys();
    }

}
