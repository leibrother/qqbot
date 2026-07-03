package cc.rapidev.qqbot.extension.settings.persistence.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;

/**
 * @author leibrother
 */
public class SettingRepository extends SimpleRepository<SettingEntity> {

    public SettingRepository(BotDatabase database) {
        super(database, SettingEntity.class);
    }

}
