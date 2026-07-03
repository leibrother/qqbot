package cc.rapidev.qqbot.database.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.SQL;
import cc.rapidev.qqbot.database.entity.Table;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 实现了部分通用的SQL语句
 *
 * @author leibrother
 */
public abstract class SimpleRepository<T> {

    protected final BotDatabase database;
    protected final Class<T> entityClass;
    protected final Table table;

    public SimpleRepository(BotDatabase database, Class<T> entityClass) {
        this.database = database;
        this.entityClass = entityClass;
        this.table = this.database.register(entityClass);
    }

    private String buildWhere(Map<String, Object> cols) {
        StringBuilder where = new StringBuilder();
        where.append(" WHERE ");
        Iterator<String> columns = cols.keySet().iterator();
        while (columns.hasNext()) {
            String name = columns.next();
            where.append("`").append(name).append("` = ?");
            if (columns.hasNext()) {
                where.append(" AND ");
            }
        }
        return where.toString();
    }

    /**
     * 查询记录条数
     *
     * @return 条数
     */
    public int count() {
        return count(null);
    }

    /**
     * 根据列条件查询数据条数
     *
     * @param cols 列条件 key = column name, value = column name
     * @return 条数
     */
    public int count(Map<String, Object> cols) {
        String base = table.getSQL(SQL.COUNT);
        return this.database.execute((handle) -> {
            if (cols == null || cols.isEmpty()) {
                return handle.select(base).mapTo(Integer.class).findFirst().orElse(0);
            }
            return handle.select(base + buildWhere(cols), cols.values().toArray()).mapTo(Integer.class).findFirst().orElse(0);
        });
    }

    /**
     * 根据列条件判断是否存在
     *
     * @param cols 列条件
     * @return 是否存在记录
     */
    public boolean exists(Map<String, Object> cols) {
        return count(cols) > 0;
    }

    /**
     * 判断bean是否存在
     *
     * @param bean bean
     * @return 是否存在
     */
    public boolean exists(Object bean) {
        String statement = table.getSQL(SQL.EXISTS_BY_PRIMARY_KEYS);
        return this.database.execute((handle) -> {
            ResultIterable<Integer> result = handle.createQuery(statement).bindBean(bean).mapTo(Integer.class);
            return result.findFirst().orElse(0) > 0;
        });
    }

    /**
     * 查询所有数据
     *
     * @return 实体列表
     */
    public List<T> find() {
        return find(null);
    }

    /**
     * 根据列条件查询记录
     *
     * @param cols 列条件
     * @return 实体列表
     */
    public List<T> find(Map<String, Object> cols) {
        String base = table.getSQL(SQL.SELECT);
        return this.database.execute((handle) -> {
            if (cols == null || cols.isEmpty()) {
                return handle.select(base).mapToBean(entityClass).list();
            }
            return handle.select(base + buildWhere(cols), cols.values().toArray()).mapToBean(entityClass).list();
        });
    }

    /**
     * 查询单条记录
     * <p>如果存在多条会抛出异常</p>
     *
     * @param cols 列条件
     * @return Optional
     */
    public Optional<T> findOne(Map<String, Object> cols) {
        List<T> list = find(cols);
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() == 1) {
            return Optional.of(list.getFirst());
        } else {
            throw new IllegalStateException("You only need one record, but find multiple records!");
        }
    }

    /**
     * 保存bean
     *
     * @param bean bean
     */
    public void store(T bean) {
        if (exists(bean)) {
            update(bean);
        } else {
            insert(bean);
        }
    }

    /**
     * 插入记录
     *
     * @param bean bean
     */
    public void insert(Object bean) {
        this.database.update(table.getSQL(SQL.INSERT), bean);
    }

    /**
     * 更新记录
     *
     * @param bean bean
     */
    public void update(Object bean) {
        this.database.update(table.getSQL(SQL.UPDATE_BY_PRIMARY_KEYS), bean);
    }

    /**
     * 删除记录
     *
     * @param bean bean
     */
    public void delete(Object bean) {
        this.database.update(table.getSQL(SQL.DELETE_BY_PRIMARY_KEYS), bean);
    }

}
