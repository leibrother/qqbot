package cc.rapidev.qqbot.rocokingdom.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;
import cc.rapidev.qqbot.rocokingdom.repository.entity.MerchantPushLog;

/**
 * @author leibrother
 */
public class MerchantPushLogRepository extends SimpleRepository<MerchantPushLog> {

    public MerchantPushLogRepository(BotDatabase database) {
        super(database, MerchantPushLog.class);
    }

}
