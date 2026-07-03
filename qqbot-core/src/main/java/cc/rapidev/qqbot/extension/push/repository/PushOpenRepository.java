package cc.rapidev.qqbot.extension.push.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;

/**
 * @author leibrother
 */
public class PushOpenRepository extends SimpleRepository<PushOpenEntity> {

    public PushOpenRepository(BotDatabase database) {
        super(database, PushOpenEntity.class);
    }

}
