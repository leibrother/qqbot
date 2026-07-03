package cc.rapidev.qqbot.database.repository.parameter;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class ParameterRepository extends SimpleRepository<ParameterEntity> {

    public ParameterRepository(BotDatabase database) {
        super(database, ParameterEntity.class);
    }

    public void set(String key, Object value) {
        ParameterEntity parameter = ParameterEntity.of(key, value);
        this.database.update("""
                INSERT INTO %s(key,value,type) VALUES(:key,:value,:type)
                ON CONFLICT(key) DO UPDATE SET value = excluded.value,type = excluded.type
                """.formatted(table.name()), parameter);
    }

    public Optional<Object> get(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        return findOne(map).map(ParameterEntity::resolve);
    }

    public void delete(String key) {
        this.database.execute("DELETE FROM %s WHERE key = ?".formatted(table.name()), key);
    }

    public Optional<Object> pop(String key) {
        Optional<Object> optional = get(key);
        if (optional.isPresent()) {
            delete(key);
        }
        return optional;
    }

}
