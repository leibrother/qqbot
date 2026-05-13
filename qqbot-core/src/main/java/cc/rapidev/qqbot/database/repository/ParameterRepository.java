package cc.rapidev.qqbot.database.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.table.TableDefinition;

import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class ParameterRepository {

    private final BotDatabase db;
    private final TableDefinition table;

    public ParameterRepository(BotDatabase db) {
        this.db = db;
        this.table = db.register(Parameter.class);
    }

    public void set(String key, Object value) {
        Parameter parameter = Parameter.of(key, value);
        db.update("""
                INSERT INTO %s(key,value,type) VALUES(:key,:value,:type)
                ON CONFLICT(key) DO UPDATE SET value = excluded.value,type = excluded.type
                """.formatted(table.name()), parameter);
    }

    public Optional<Object> get(String key) {
        Optional<Map<String, Object>> optional = db.one("SELECT * FROM %s WHERE key = ?".formatted(table.name()), key);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Object> map = optional.get();
        String type = map.get("type").toString();
        String value = map.get("value").toString();
        return Optional.of(ParameterType.valueOf(type).getDecoder().apply(value));
    }

    public void delete(String key) {
        db.execute("DELETE FROM %s WHERE key = ?".formatted(table.name()), key);
    }

    public Optional<Object> pop(String key) {
        Optional<Object> optional = get(key);
        if (optional.isPresent()) {
            delete(key);
        }
        return optional;
    }

}
