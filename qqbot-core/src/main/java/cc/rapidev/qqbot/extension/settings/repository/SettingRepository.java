package cc.rapidev.qqbot.extension.settings.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.Table;
import cc.rapidev.qqbot.extension.settings.repository.entity.SettingEntity;
import com.google.common.collect.Maps;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leibrother
 */
public class SettingRepository {

    private final BotDatabase database;
    private final Table table;
    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public SettingRepository(BotDatabase database) {
        this.database = database;
        this.table = this.database.register(SettingEntity.class);
    }

    private void putCache(String key, String scope, String value) {
        cache.computeIfAbsent(key, k -> Maps.newHashMap()).put(scope, value);
    }

    public String get(String key, String scope) {
        if (cache.containsKey(key) && cache.get(key).containsKey(scope)) {
            return cache.get(key).get(scope);
        }
        return database.execute(handle -> {
            ResultIterable<SettingEntity> result = handle.select("""
                    SELECT * FROM %s WHERE key = ? AND scope = ?
                    """.formatted(table.safename()), key, scope).mapToBean(SettingEntity.class);
            List<SettingEntity> list = result.list();
            if (list.isEmpty()) {
                return null;
            } else {
                String value = list.getFirst().getValue();
                putCache(key, scope, value);
                return value;
            }
        });
    }

    public void set(String key, String scope, String value) {
        SettingEntity entity = new SettingEntity(key, scope, value);
        this.database.update("""
                INSERT INTO %s (key, scope, value) VALUES(:key, :scope, :value)
                ON CONFLICT(key, scope) DO UPDATE SET value = excluded.value
                """.formatted(table.name()), entity);
        putCache(key, scope, value);
    }

}
