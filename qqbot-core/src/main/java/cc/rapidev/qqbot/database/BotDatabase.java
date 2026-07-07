package cc.rapidev.qqbot.database;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.database.entity.EntityAnalyzer;
import cc.rapidev.qqbot.database.entity.Table;
import cc.rapidev.qqbot.database.entity.TableColumn;
import cc.rapidev.qqbot.database.repository.parameter.ParameterRepository;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author leibrother
 */
public class BotDatabase {

    private final Jdbi jdbi;
    private final ParameterRepository parameterRepository;

    public BotDatabase(Bot bot) {
        Path datadir = bot.datadir();
        String appid = bot.config().getAppid();
        DataSource dataSource = SQLiteDataSourceFactory.create(datadir, appid);
        this.jdbi = Jdbi.create(dataSource)
                .setSqlLogger(new SQLLogger())
                .installPlugin(new SQLitePlugin())
                .installPlugin(new SqlObjectPlugin());
        this.parameterRepository = new ParameterRepository(this);
    }

    public ParameterRepository parameters() {
        return this.parameterRepository;
    }

    /**
     * 注册一个数据库表，自动生成其表结构
     *
     * @param clazz 数据库表对应的类，需被<code>@DBTable</code>注解
     */
    public synchronized Table register(Class<?> clazz) {
        Table table = EntityAnalyzer.analyze(clazz);
        String name = table.name();
        if (tableIfExists(name)) {
            // 表存在，进行差异更新
            List<TableColumn> columns = tableColumnsSchema(name);
            List<String> sqls = table.diffUpdate(columns);
            if (!sqls.isEmpty()) {
                this.transaction(sqls);
            }
        } else {
            // 表不存在，执行建表语句
            execute(table.schema());
        }
        return table;
    }

    /**
     * 检查表是否存在
     *
     * @param name 表名称
     * @return 存在为true，不存在为false
     */
    public boolean tableIfExists(String name) {
        return one("SELECT 1 FROM sqlite_master WHERE type='table' AND name = ?", name).isPresent();
    }

    private List<TableColumn> tableColumnsSchema(String name) {
        List<Map<String, Object>> res = select("PRAGMA table_info(%s)".formatted(name));
        return res.stream()
                .map(row -> {
                    Object defaultValue = row.get("dflt_value");
                    return new TableColumn(
                            row.get("name").toString(),
                            row.get("type").toString(),
                            "1".equals(row.get("notnull").toString()),
                            defaultValue == null ? "" : defaultValue.toString(),
                            Integer.parseInt(row.get("pk").toString()) > 0
                    );
                }).toList();
    }

    /**
     * 执行SQL
     *
     * @param callback 回调
     * @return 执行结果
     */
    public <R> R execute(Function<Handle, R> callback) {
        return jdbi.withHandle(callback::apply);
    }

    /**
     * 执行SQL
     *
     * @param sql  SQL
     * @param args 参数
     */
    public void execute(String sql, Object... args) {
        execute((handle) -> handle.execute(sql, args));
    }

    /**
     * 在事物中执行
     *
     * @param callback 回调
     */
    public void transaction(Consumer<Handle> callback) {
        jdbi.useTransaction(callback::accept);
    }

    /**
     * 在事务中执行SQL
     *
     * @param sqls SQL语句
     */
    public void transaction(List<String> sqls) {
        transaction((handle) -> sqls.forEach(handle::execute));
    }

    /**
     * 查询
     *
     * @param sql  SQL
     * @param args 参数
     * @return 映射为MAP的结果集
     */
    public List<Map<String, Object>> select(String sql, Object... args) {
        return execute((handle) -> handle.select(sql, args).mapToMap().list());
    }

    /**
     * 查询单条记录
     *
     * @param sql  SQL
     * @param args 参数
     * @return 映射为MAP的结果，不存在时返回<code>Optional.empty()</code>
     */
    public Optional<Map<String, Object>> one(String sql, Object... args) {
        List<Map<String, Object>> result = select(sql, args);
        return result.stream().findFirst();
    }

    /**
     * 执行更新语句
     *
     * @param sql  INSERT/UPDATE/其他更新语句
     * @param bean 需绑定的对象，通过字段名绑定
     */
    public void update(String sql, Object bean) {
        execute((handle) -> handle.createUpdate(sql).bindBean(bean).execute());
    }

}
