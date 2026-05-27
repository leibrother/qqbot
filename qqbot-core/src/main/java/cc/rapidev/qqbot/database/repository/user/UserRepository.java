package cc.rapidev.qqbot.database.repository.user;

import cc.rapidev.qqbot.database.BotDatabase;
import cc.rapidev.qqbot.database.entity.Table;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.List;

/**
 * @author leibrother
 */
public class UserRepository {

    private final BotDatabase database;
    private final Table definition;

    public UserRepository(BotDatabase database) {
        this.database = database;
        this.definition = this.database.register(UserEntity.class);
    }

    public UserEntity findByOpenId(String openid) {
        List<UserEntity> list = findByOpenIds(List.of(openid));
        return list.isEmpty() ? null : list.getFirst();
    }

    public List<UserEntity> findByOpenIds(List<String> ids) {
        return database.execute(handle -> {
            ResultIterable<UserEntity> iterable = handle.select("""
                    SELECT * FROM %s WHERE openid in (?)
                    """.formatted(definition.name()), String.join(",", ids)).mapToBean(UserEntity.class);
            return iterable.stream().toList();
        });
    }

    public void store(UserEntity entity) {
        UserEntity exist = findByOpenId(entity.getOpenid());
        if (exist == null) {
            this.database.update("""
                    INSERT INTO %s (openid, avatar, username, bot) VALUES(:openid, :avatar, :username, :bot)
                    """.formatted(definition.name()), entity);
        } else {
            if (StringUtils.isEmpty(entity.getAvatar())) {
                entity.setAvatar(exist.getAvatar());
            }
            if (StringUtils.isEmpty(entity.getUsername())) {
                entity.setUsername(exist.getUsername());
            }
            this.database.update("""
                    UPDATE %s SET avatar = :avatar, username = :username WHERE openid = :openid
                    """.formatted(definition.name()), entity);
        }
    }

}
