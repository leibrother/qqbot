package cc.rapidev.qqbot.rocokingdom.repository;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.repository.SimpleRepository;

import java.util.List;

/**
 * @author leibrother
 */
public class RocokingdomDexRepository extends SimpleRepository<RocokingdomDex> {

    public RocokingdomDexRepository(BotDatabase database) {
        super(database, RocokingdomDex.class);
    }

    public List<RocokingdomDex> findByFullnameLike(String fullname) {
        return this.database.execute((handle) ->
                handle.select("SELECT * FROM %s WHERE `fullname` LIKE ?".formatted(table.name()), "%" + fullname + "%")
                        .mapToBean(entityClass)
                        .list()
        );
    }

}
