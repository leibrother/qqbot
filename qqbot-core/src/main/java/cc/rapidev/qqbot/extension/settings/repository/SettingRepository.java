package cc.rapidev.qqbot.extension.settings.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.Table;
import cc.rapidev.qqbot.extension.settings.repository.entity.SettingEntity;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.List;

/**
 * @author leibrother
 */
public class SettingRepository {

    private final BotDatabase database;
    private final Table table;

    public SettingRepository(BotDatabase database) {
        this.database = database;
        this.table = this.database.register(SettingEntity.class);
    }

    public String get(String key, String scope) {
        return database.execute(handle -> {
            ResultIterable<SettingEntity> result = handle.select("""
                    SELECT * FROM %s WHERE key = ? AND scope = ?
                    """.formatted(table.safename()), key, scope).mapToBean(SettingEntity.class);
            List<SettingEntity> list = result.list();
            if (list.isEmpty()) {
                return null;
            } else {
                return list.getFirst().getValue();
            }
        });
    }

    public void set(String key, String scope, String value) {
        SettingEntity entity = new SettingEntity(key, scope, value);
        this.database.update("""
                INSERT INTO %s (key, scope, value) VALUES(:key, :scope, :value)
                ON CONFLICT(key, scope) DO UPDATE SET value = excluded.value
                """.formatted(table.name()), entity);
    }

}
