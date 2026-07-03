package cc.rapidev.qqbot.extension.settings.persistence;

import cc.rapidev.qqbot.Bot;
import cc.rapidev.qqbot.common.annotations.Param;
import cc.rapidev.qqbot.common.cache.CacheEvict;
import cc.rapidev.qqbot.common.cache.Cacheable;
import cc.rapidev.qqbot.extension.settings.component.SettingItem;
import cc.rapidev.qqbot.extension.settings.persistence.repository.SettingEntity;
import cc.rapidev.qqbot.extension.settings.persistence.repository.SettingRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author leibrother
 */
public class CacheableSettingPersistenceService implements SettingPersistenceService {

    private final SettingRepository repository;

    public CacheableSettingPersistenceService(Bot bot) {
        this.repository = new SettingRepository(bot.database());
    }

    @Cacheable(key = "'setting:' + setting.completedKey() + ':' + scope")
    @Override
    public String getValue(@Param("setting") SettingItem setting, @Param("scope") String scope) {
        Map<String, Object> cols = new HashMap<>();
        cols.put("key", setting.completedKey());
        cols.put("scope", scope);
        Optional<SettingEntity> result = repository.findOne(cols);
        return result.map(SettingEntity::getValue).orElse(null);
    }

    @CacheEvict(key = "'setting:' + setting.completedKey() + ':' + scope")
    @Override
    public void setValue(@Param("setting") SettingItem setting, @Param("scope") String scope, String value) {
        String key = setting.completedKey();
        SettingEntity entity = new SettingEntity(key, scope, value);
        this.repository.store(entity);
    }

}
